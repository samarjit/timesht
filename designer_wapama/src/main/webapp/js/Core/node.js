
/**
 * Copyright (c) 2006
 * Martin Czuchra, Nicolas Peters, Daniel Polak, Willi Tscheschner
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 **/

/**
 * Init namespaces
 */
if (!ORYX) {
    var ORYX = {};
}
if (!ORYX.Core) {
    ORYX.Core = {};
}

/**
 * @classDescription Abstract base class for all Nodes.
 * @extends ORYX.Core.Shape
 */
ORYX.Core.Node = {

    /**
     * Constructor
     * @param options {Object} A container for arguments.
     * @param stencil {Stencil}
     */
    construct: function(options, stencil, resourceId){
        arguments.callee.$.construct.apply(this, arguments, resourceId);
        
        this.isSelectable = true;
        this.isMovable = true;
		this._dockerUpdated = false;
        
        this._oldBounds = new ORYX.Core.Bounds(); //init bounds with undefined values
        this._svgShapes = []; //array of all SVGShape objects of
        // SVG representation
        
        //TODO vielleicht in shape verschieben?
        this.minimumSize = undefined; // {width:..., height:...}
        this.maximumSize = undefined;
        
        //TODO vielleicht in shape oder uiobject verschieben?
        // vielleicht sogar isResizable ersetzen?
        this.isHorizontallyResizable = false;
        this.isVerticallyResizable = false;
        
        this.dataId = undefined;
        
        this._init(this._stencil.view());
    },
        
    /**
     * This method checks whether the shape is resized correctly and calls the
     * super class update method.
     */
    _update: function(){
		
		this.dockers.invoke("update");
		if (this.isChanged) {

			var bounds = this.bounds;
            var oldBounds = this._oldBounds;
						
			if (this.isResized) {
			
				var widthDelta = bounds.width() / oldBounds.width();
				var heightDelta = bounds.height() / oldBounds.height();
				
				//iterate over all relevant svg elements and resize them
				this._svgShapes.each(function(svgShape){
					//adjust width
					if (svgShape.isHorizontallyResizable) {
						svgShape.width = svgShape.oldWidth * widthDelta;
					}
					//adjust height
					if (svgShape.isVerticallyResizable) {
						svgShape.height = svgShape.oldHeight * heightDelta;
					}
					
					//check, if anchors are set
					var anchorOffset;
					var leftIncluded = svgShape.anchorLeft;
					var rightIncluded = svgShape.anchorRight;
					
					if (rightIncluded) {
						anchorOffset = oldBounds.width() - (svgShape.oldX + svgShape.oldWidth);
						if (leftIncluded) {
							svgShape.width = bounds.width() - svgShape.x - anchorOffset;
						}
						else {
							svgShape.x = bounds.width() - (anchorOffset + svgShape.width);
						}
					}
					else 
						if (!leftIncluded) {
							svgShape.x = widthDelta * svgShape.oldX;
							if (!svgShape.isHorizontallyResizable) {
								svgShape.x = svgShape.x + svgShape.width * widthDelta / 2 - svgShape.width / 2;
							}
						}
					
					var topIncluded = svgShape.anchorTop;
					var bottomIncluded = svgShape.anchorBottom;
					
					if (bottomIncluded) {
						anchorOffset = oldBounds.height() - (svgShape.oldY + svgShape.oldHeight);
						if (topIncluded) {
							svgShape.height = bounds.height() - svgShape.y - anchorOffset;
						}
						else {
							// Hack for choreography task layouting
							if (!svgShape._isYLocked) {
								svgShape.y = bounds.height() - (anchorOffset + svgShape.height);
							}
						}
					}
					else 
						if (!topIncluded) {
							svgShape.y = heightDelta * svgShape.oldY;
							if (!svgShape.isVerticallyResizable) {
								svgShape.y = svgShape.y + svgShape.height * heightDelta / 2 - svgShape.height / 2;
							}
						}
				});
				
				//check, if the current bounds is unallowed horizontally or vertically resized
				var p = {
					x: 0,
					y: 0
				};
				if (!this.isHorizontallyResizable && bounds.width() !== oldBounds.width()) {
					p.x = oldBounds.width() - bounds.width();
				}
				if (!this.isVerticallyResizable && bounds.height() !== oldBounds.height()) {
					p.y = oldBounds.height() - bounds.height();
				}
				if (p.x !== 0 || p.y !== 0) {
					bounds.extend(p);
				}
				
				//check, if the current bounds are between maximum and minimum bounds
				p = {
					x: 0,
					y: 0
				};
				var widthDifference, heightDifference;
				if (this.minimumSize) {
				
					ORYX.Log.debug("Shape (%0)'s min size: (%1x%2)", this, this.minimumSize.width, this.minimumSize.height);
					widthDifference = this.minimumSize.width - bounds.width();
					if (widthDifference > 0) {
						p.x += widthDifference;
					}
					heightDifference = this.minimumSize.height - bounds.height();
					if (heightDifference > 0) {
						p.y += heightDifference;
					}
				}
				if (this.maximumSize) {
				
					ORYX.Log.debug("Shape (%0)'s max size: (%1x%2)", this, this.maximumSize.width, this.maximumSize.height);
					widthDifference = bounds.width() - this.maximumSize.width;
					if (widthDifference > 0) {
						p.x -= widthDifference;
					}
					heightDifference = bounds.height() - this.maximumSize.height;
					if (heightDifference > 0) {
						p.y -= heightDifference;
					}
				}
				if (p.x !== 0 || p.y !== 0) {
					bounds.extend(p);
				}
				
				//update magnets
				
				var widthDelta = bounds.width() / oldBounds.width();
				var heightDelta = bounds.height() / oldBounds.height();
				
				var leftIncluded, rightIncluded, topIncluded, bottomIncluded, center, newX, newY;
				
				this.magnets.each(function(magnet){
					leftIncluded = magnet.anchorLeft;
					rightIncluded = magnet.anchorRight;
					topIncluded = magnet.anchorTop;
					bottomIncluded = magnet.anchorBottom;
					
					center = magnet.bounds.center();
					
					if (leftIncluded) {
						newX = center.x;
					}
					else 
						if (rightIncluded) {
							newX = bounds.width() - (oldBounds.width() - center.x)
						}
						else {
							newX = center.x * widthDelta;
						}
					
					if (topIncluded) {
						newY = center.y;
					}
					else 
						if (bottomIncluded) {
							newY = bounds.height() - (oldBounds.height() - center.y);
						}
						else {
							newY = center.y * heightDelta;
						}
					
					if (center.x !== newX || center.y !== newY) {
						magnet.bounds.centerMoveTo(newX, newY);
					}
				});
				
				//set new position of labels
				this.getLabels().each(function(label){
					leftIncluded = label.anchorLeft;
					rightIncluded = label.anchorRight;
					topIncluded = label.anchorTop;
					bottomIncluded = label.anchorBottom;
					
					
					if (leftIncluded) {
					
					}
					else 
						if (rightIncluded) {
							label.x = bounds.width() - (oldBounds.width() - label.oldX)
						}
						else {
							label.x *= widthDelta;
						}
					
					if (topIncluded) {
					
					}
					else 
						if (bottomIncluded) {
							label.y = bounds.height() - (oldBounds.height() - label.oldY);
						}
						else {
							label.y *= heightDelta;
						}
				});
				
				//update docker
				var docker = this.dockers[0];
				if (docker) {
					docker.bounds.unregisterCallback(this._dockerChangedCallback);
					if (!this._dockerUpdated) {
						docker.bounds.centerMoveTo(this.bounds.center());
						this._dockerUpdated = false;
					}
					
					docker.update();
					docker.bounds.registerCallback(this._dockerChangedCallback);
				}
				this.isResized = false;
			}
            
            this.refresh();
			
			this.isChanged = false;
			
			this._oldBounds = this.bounds.clone();
        }
		
		this.children.each(function(value) {
			if(!(value instanceof ORYX.Core.Controls.Docker)) {
				value._update();
			}
		});
		
		if (this.dockers.length > 0&&!this.dockers.first().getDockedShape()) {
			this.dockers.each(function(docker){
				docker.bounds.centerMoveTo(this.bounds.center())
			}.bind(this))
		}
		
		/*this.incoming.each((function(edge) {
			if(!(this.dockers[0] && this.dockers[0].getDockedShape() instanceof ORYX.Core.Node))
				edge._update(true);
		}).bind(this));
		
		this.outgoing.each((function(edge) {
			if(!(this.dockers[0] && this.dockers[0].getDockedShape() instanceof ORYX.Core.Node))
				edge._update(true);
		}).bind(this)); */
    },
    
    /**
     * This method repositions and resizes the SVG representation
     * of the shape.
     */
    refresh: function(){
        arguments.callee.$.refresh.apply(this, arguments);
        
        /** Movement */
        var x = this.bounds.upperLeft().x;
        var y = this.bounds.upperLeft().y;
        
        //set translation in transform attribute
        /*var attributeTransform = document.createAttributeNS(ORYX.CONFIG.NAMESPACE_SVG, "transform");
        attributeTransform.nodeValue = "translate(" + x + ", " + y + ")";
        this.node.firstChild.setAttributeNode(attributeTransform);*/
		// Move owner element
		this.node.firstChild.setAttributeNS(null, "transform", "translate(" + x + ", " + y + ")");
		// Move magnets
		this.node.childNodes[1].childNodes[1].setAttributeNS(null, "transform", "translate(" + x + ", " + y + ")");
        
        /** Resize */
        
        //iterate over all relevant svg elements and update them
        this._svgShapes.each(function(svgShape){
            svgShape.update();
        });
    },
    
    _dockerChanged: function(){
		var docker = this.dockers[0];
        
        //set the bounds of the the association
        this.bounds.centerMoveTo(docker.bounds.center());
        
		this._dockerUpdated = true;
        //this._update(true);
    },
    
    /**
     * This method traverses a tree of SVGElements and returns
     * all SVGShape objects. For each basic shape or path element
     * a SVGShape object is initialized.
     *
     * @param svgNode {SVGElement}
     * @return {Array} Array of SVGShape objects
     */
    _initSVGShapes: function(svgNode){
        var svgShapes = [];
        try {
            var svgShape = new ORYX.Core.SVG.SVGShape(svgNode);
            svgShapes.push(svgShape);
        } 
        catch (e) {
            //do nothing
        }
        
        if (svgNode.hasChildNodes()) {
            for (var i = 0; i < svgNode.childNodes.length; i++) {
                svgShapes = svgShapes.concat(this._initSVGShapes(svgNode.childNodes[i]));
            }
        }
        
        return svgShapes;
    },
    
    /**
     * Calculate if the point is inside the Shape
     * @param {PointX}
     * @param {PointY} 
     * @param {absoluteBounds} optional: for performance
     */
    isPointIncluded: function(pointX, pointY, absoluteBounds){
        // If there is an arguments with the absoluteBounds
        var absBounds = absoluteBounds && absoluteBounds instanceof ORYX.Core.Bounds ? absoluteBounds : this.absoluteBounds();
        
        if (!absBounds.isIncluded(pointX, pointY)) {
			return false;
		} else {
			
		}
			
        
        //point = Object.clone(point);
        var ul = absBounds.upperLeft();
        var x = pointX - ul.x;
        var y = pointY - ul.y;		
	
		var i=0;
		do {
			var isPointIncluded = this._svgShapes[i++].isPointIncluded( x, y );
		} while( !isPointIncluded && i < this._svgShapes.length)
		
		return isPointIncluded;

        /*return this._svgShapes.any(function(svgShape){
            return svgShape.isPointIncluded(point);
        });*/
    },
 
    
    /**
     * Calculate if the point is over an special offset area
     * @param {Point}
     */
    isPointOverOffset: function( pointX, pointY ){       
		var isOverEl = arguments.callee.$.isPointOverOffset.apply( this , arguments );
		
		if (isOverEl) {
						
	        // If there is an arguments with the absoluteBounds
	        var absBounds = this.absoluteBounds();
	        absBounds.widen( - ORYX.CONFIG.BORDER_OFFSET );
			
	        if ( !absBounds.isIncluded( pointX, pointY )) {
	            return true;
	        }		
		}
		
		return false;
		
	},
	   
    serialize: function(){
        var result = arguments.callee.$.serialize.apply(this);
        
        // Add the docker's bounds
        // nodes only have at most one docker!
        this.dockers.each((function(docker){
			if (docker.getDockedShape()) {
				var center = docker.referencePoint;
				center = center ? center : docker.bounds.center();
				result.push({
					name: 'docker',
					prefix: 'oryx',
					value: $H(center).values().join(','),
					type: 'literal'
				});
			}
        }).bind(this));
        
        // Get the spezific serialized object from the stencil
        try {
            //result = this.getStencil().serialize(this, result);

			var serializeEvent = this.getStencil().serialize();
			
			/*
			 * call serialize callback by reference, result should be found
			 * in serializeEvent.result
			 */
			if(serializeEvent.type) {
				serializeEvent.shape = this;
				serializeEvent.data = result;
				serializeEvent.result = undefined;
				serializeEvent.forceExecution = true;
				
				this._delegateEvent(serializeEvent);
				
				if(serializeEvent.result) {
					result = serializeEvent.result;
				}
			}
        } 
        catch (e) {
        }
        return result;
    },
    
    deserialize: function(data){
    	arguments.callee.$.deserialize.apply(this, [data]);
		
	    try {
            //data = this.getStencil().deserialize(this, data);

			var deserializeEvent = this.getStencil().deserialize();
			
			/*
			 * call serialize callback by reference, result should be found
			 * in serializeEventInfo.result
			 */
			if(deserializeEvent.type) {
				deserializeEvent.shape = this;
				deserializeEvent.data = data;
				deserializeEvent.result = undefined;
				deserializeEvent.forceExecution = true;
				
				this._delegateEvent(deserializeEvent);
				if(deserializeEvent.result) {
					data = deserializeEvent.result;
				}
			}
        } 
        catch (e) {
        }
		
		// Set the outgoing shapes
		var outgoing = data.findAll(function(ser){ return (ser.prefix+"-"+ser.name) == 'raziel-outgoing'});
		outgoing.each((function(obj){
			// TODO: Look at Canvas
			if(!this.parent) {return};
								
			// Set outgoing Shape
			var next = this.getCanvas().getChildShapeByResourceId(obj.value);
																	
			if(next){
				if(next instanceof ORYX.Core.Edge) {
					//Set the first docker of the next shape
					next.dockers.first().setDockedShape(this);
					next.dockers.first().setReferencePoint(next.dockers.first().bounds.center());
				} else if(next.dockers.length > 0) { //next is a node and next has a docker
					next.dockers.first().setDockedShape(this);
					//next.dockers.first().setReferencePoint({x: this.bounds.width() / 2.0, y: this.bounds.height() / 2.0});
				}
			}	
			
		}).bind(this));
        
        if (this.dockers.length === 1) {
            var dockerPos;
            dockerPos = data.find(function(entry){
                return (entry.prefix + "-" + entry.name === "oryx-dockers");
            });
            
            if (dockerPos) {
                var points = dockerPos.value.replace(/,/g, " ").split(" ").without("").without("#");
				if (points.length === 2 && this.dockers[0].getDockedShape()) {
                    this.dockers[0].setReferencePoint({
                        x: parseFloat(points[0]),
                        y: parseFloat(points[1])
                    });
                }
                else {
                    this.dockers[0].bounds.centerMoveTo(parseFloat(points[0]), parseFloat(points[1]));
                }
            }
        }
    },
    
    /**
     * This method excepts the SVGDoucment that is the SVG representation
     * of this shape.
     * The bounds of the shape are calculated, the SVG representation's upper left point
     * is moved to 0,0 and it the method sets if this shape is resizable.
     *
     * @param {SVGDocument} svgDocument
     */
    _init: function(svgDocument){
        arguments.callee.$._init.apply(this, arguments);
		
        var svgNode = svgDocument.getElementsByTagName("g")[0]; //outer most g node
        // set all required attributes
        var attributeTitle = svgDocument.ownerDocument.createAttributeNS(null, "title");
        attributeTitle.nodeValue = this.getStencil().title();
        svgNode.setAttributeNode(attributeTitle);
        
        var attributeId = svgDocument.ownerDocument.createAttributeNS(null, "id");
        attributeId.nodeValue = this.id;
        svgNode.setAttributeNode(attributeId);
        
        // 
        var stencilTargetNode = this.node.childNodes[0].childNodes[0]; //<g class=me>"
        svgNode = stencilTargetNode.appendChild(svgNode);
        
        // Add to the EventHandler
        this.addEventHandlers(svgNode);
        
        /**set minimum and maximum size*/
        var minSizeAttr = svgNode.getAttributeNS(ORYX.CONFIG.NAMESPACE_ORYX, "minimumSize");
        if (minSizeAttr) {
            minSizeAttr = minSizeAttr.replace("/,/g", " ");
            var minSizeValues = minSizeAttr.split(" ");
            minSizeValues = minSizeValues.without("");
            
            if (minSizeValues.length > 1) {
                this.minimumSize = {
                    width: parseFloat(minSizeValues[0]),
                    height: parseFloat(minSizeValues[1])
                };
            }
            else {
                //set minimumSize to (1,1), so that width and height of the stencil can never be (0,0)
                this.minimumSize = {
                    width: 1,
                    height: 1
                };
            }
        }
        
        var maxSizeAttr = svgNode.getAttributeNS(ORYX.CONFIG.NAMESPACE_ORYX, "maximumSize");
        if (maxSizeAttr) {
            maxSizeAttr = maxSizeAttr.replace("/,/g", " ");
            var maxSizeValues = maxSizeAttr.split(" ");
            maxSizeValues = maxSizeValues.without("");
            
            if (maxSizeValues.length > 1) {
                this.maximumSize = {
                    width: parseFloat(maxSizeValues[0]),
                    height: parseFloat(maxSizeValues[1])
                };
            }
        }
        
        if (this.minimumSize && this.maximumSize &&
        (this.minimumSize.width > this.maximumSize.width ||
        this.minimumSize.height > this.maximumSize.height)) {
        
            //TODO wird verschluckt!!!
            throw this + ": Minimum Size must be greater than maxiumSize.";
        }
        
        /**get current bounds and adjust it to upperLeft == (0,0)*/
        //initialize all SVGShape objects
        this._svgShapes = this._initSVGShapes(svgNode);
        
        //get upperLeft and lowerRight of stencil
        var upperLeft = {
            x: undefined,
            y: undefined
        };
        var lowerRight = {
            x: undefined,
            y: undefined
        };
        var me = this;
        this._svgShapes.each(function(svgShape){
            upperLeft.x = (upperLeft.x !== undefined) ? Math.min(upperLeft.x, svgShape.x) : svgShape.x;
            upperLeft.y = (upperLeft.y !== undefined) ? Math.min(upperLeft.y, svgShape.y) : svgShape.y;
            lowerRight.x = (lowerRight.x !== undefined) ? Math.max(lowerRight.x, svgShape.x + svgShape.width) : svgShape.x + svgShape.width;
            lowerRight.y = (lowerRight.y !== undefined) ? Math.max(lowerRight.y, svgShape.y + svgShape.height) : svgShape.y + svgShape.height;
            
            /** set if resizing is enabled */
            //TODO isResizable durch die beiden anderen booleans ersetzen?
            if (svgShape.isHorizontallyResizable) {
                me.isHorizontallyResizable = true;
                me.isResizable = true;
            }
            if (svgShape.isVerticallyResizable) {
                me.isVerticallyResizable = true;
                me.isResizable = true;
            }
            if (svgShape.anchorTop && svgShape.anchorBottom) {
                me.isVerticallyResizable = true;
                me.isResizable = true;
            }
            if (svgShape.anchorLeft && svgShape.anchorRight) {
                me.isHorizontallyResizable = true;
                me.isResizable = true;
            }
        });
        
        //move all SVGShapes by -upperLeft
        this._svgShapes.each(function(svgShape){
            svgShape.x -= upperLeft.x;
            svgShape.y -= upperLeft.y;
            svgShape.update();
        });
        
        //set bounds of shape
        //the offsets are also needed for positioning the magnets and the docker
        var offsetX = upperLeft.x;
        var offsetY = upperLeft.y;
        
        lowerRight.x -= offsetX;
        lowerRight.y -= offsetY;
        upperLeft.x = 0;
        upperLeft.y = 0;
        
        //prevent that width or height of initial bounds is 0
        if (lowerRight.x === 0) {
            lowerRight.x = 1;
        }
        if (lowerRight.y === 0) {
            lowerRight.y = 1;
        }
        
        this._oldBounds.set(upperLeft, lowerRight);
        this.bounds.set(upperLeft, lowerRight);
        
        /**initialize magnets */
        
        var magnets = svgDocument.getElementsByTagNameNS(ORYX.CONFIG.NAMESPACE_ORYX, "magnets");
        
        if (magnets && magnets.length > 0) {
        
            magnets = $A(magnets[0].getElementsByTagNameNS(ORYX.CONFIG.NAMESPACE_ORYX, "magnet"));
            
            var me = this;
            magnets.each(function(magnetElem){
                var magnet = new ORYX.Core.Controls.Magnet({
                    eventHandlerCallback: me.eventHandlerCallback
                });
                var cx = parseFloat(magnetElem.getAttributeNS(ORYX.CONFIG.NAMESPACE_ORYX, "cx"));
                var cy = parseFloat(magnetElem.getAttributeNS(ORYX.CONFIG.NAMESPACE_ORYX, "cy"));
                magnet.bounds.centerMoveTo({
                    x: cx - offsetX,
                    y: cy - offsetY
                });
                
                //get anchors
                var anchors = magnetElem.getAttributeNS(ORYX.CONFIG.NAMESPACE_ORYX, "anchors");
                if (anchors) {
                    anchors = anchors.replace("/,/g", " ");
                    anchors = anchors.split(" ").without("");
                    for(var i = 0; i < anchors.length; i++) {
						switch(anchors[i].toLowerCase()) {
							case "left":
								magnet.anchorLeft = true;
								break;
							case "right":
								magnet.anchorRight = true;
								break;
							case "top":
								magnet.anchorTop = true;
								break;
							case "bottom":
								magnet.anchorBottom = true;
								break;
						}
					}
                }
                
                me.add(magnet);
                
                //check, if magnet is default magnet
                if (!this._defaultMagnet) {
                    var defaultAttr = magnetElem.getAttributeNS(ORYX.CONFIG.NAMESPACE_ORYX, "default");
                    if (defaultAttr && defaultAttr.toLowerCase() === "yes") {
                        me._defaultMagnet = magnet;
                    }
                }
            });
        }
        else {
            // Add a Magnet in the Center of Shape			
            var magnet = new ORYX.Core.Controls.Magnet();
            magnet.bounds.centerMoveTo(this.bounds.width() / 2, this.bounds.height() / 2);
            this.add(magnet);
        }
        
        /**initialize docker */
        var dockerElem = svgDocument.getElementsByTagNameNS(ORYX.CONFIG.NAMESPACE_ORYX, "docker");
        
        if (dockerElem && dockerElem.length > 0) {
            dockerElem = dockerElem[0];
            var docker = this.createDocker();
            var cx = parseFloat(dockerElem.getAttributeNS(ORYX.CONFIG.NAMESPACE_ORYX, "cx"));
            var cy = parseFloat(dockerElem.getAttributeNS(ORYX.CONFIG.NAMESPACE_ORYX, "cy"));
            docker.bounds.centerMoveTo({
                x: cx - offsetX,
                y: cy - offsetY
            });
            
            //get anchors
            var anchors = dockerElem.getAttributeNS(ORYX.CONFIG.NAMESPACE_ORYX, "anchors");
            if (anchors) {
                anchors = anchors.replace("/,/g", " ");
                anchors = anchors.split(" ").without("");
                
				for(var i = 0; i < anchors.length; i++) {
					switch(anchors[i].toLowerCase()) {
						case "left":
							docker.anchorLeft = true;
							break;
						case "right":
							docker.anchorRight = true;
							break;
						case "top":
							docker.anchorTop = true;
							break;
						case "bottom":
							docker.anchorBottom = true;
							break;
					}
				}
            }
        }
        
        /**initialize labels*/
        // Add a text node in each SVG of Pipe shape.
        if (this.getStencil().namespace() == ORYX.CONFIG.NAMESPACE_INTALIO_PIPES) {
            var pipeLabelTextNode = document.createElementNS(ORYX.CONFIG.NAMESPACE_SVG, "text");
            pipeLabelTextNode.setAttribute("id", svgNode.id + 'text_label');
            pipeLabelTextNode.setAttribute("font-size", "11");
            pipeLabelTextNode.setAttribute("x", this.bounds.width() / 2 + offsetX);
            pipeLabelTextNode.setAttribute("y", this.bounds.height() + 8 + offsetY);
            pipeLabelTextNode.setAttributeNS("http://www.b3mn.org/oryx", "oryx:align", "middle center");
            pipeLabelTextNode.setAttributeNS("http://www.b3mn.org/oryx", "oryx:fittoelem", "unvisibleBorder");
            pipeLabelTextNode.setAttribute("stroke", "black");
            svgNode.appendChild(pipeLabelTextNode);
        }
        var textElems = svgNode.getElementsByTagNameNS(ORYX.CONFIG.NAMESPACE_SVG, 'text');
        $A(textElems).each((function(textElem){
            var label = new ORYX.Core.SVG.Label({
                textElement: textElem,
				shapeId: this.id
            });
            label.x -= offsetX;
            label.y -= offsetY;
            this._labels[label.id] = label;
        }).bind(this));
    },
	
	/**
	 * Override the Method, that a docker is not shown
	 *
	 */
	createDocker: function() {
		var docker = new ORYX.Core.Controls.Docker({eventHandlerCallback: this.eventHandlerCallback});
		docker.bounds.registerCallback(this._dockerChangedCallback);
		
		this.dockers.push( docker );
		docker.parent = this;
		docker.bounds.registerCallback(this._changedCallback);		
		
		return docker		
	},	
    
    toString: function(){
        return this._stencil.title() + " " + this.id
    },
    
    /**
     * Set visibility for the node.
     * Node visible if isVisible is TRUE
     * Node invisible if isVisible is FALSE
     * 
     * @param {boolean} isVisible
     */
    setVisible: function(isVisible){
        if (isVisible) {
            this.node.setAttributeNS(null, 'display', 'inherit');
        } else {
            this.node.setAttributeNS(null, 'display', 'none');
        }
    }
};
ORYX.Core.Node = ORYX.Core.Shape.extend(ORYX.Core.Node);
