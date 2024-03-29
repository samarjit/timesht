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
if(!ORYX) {var ORYX = {};}
if(!ORYX.Core) {ORYX.Core = {};}

/**
 * @classDescription Base class for Shapes.
 * @extends ORYX.Core.AbstractShape
 */
ORYX.Core.Shape = {

	/**
	 * Constructor
	 */
	construct: function(options, stencil, resourceId) {
		// call base class constructor
		arguments.callee.$.construct.apply(this, arguments);
		
		this.dockers = [];
		this.magnets = [];
		
		this._defaultMagnet;
		
		this.incoming = [];
		this.outgoing = [];
		
		this.nodes = [];
		
		this._dockerChangedCallback = this._dockerChanged.bind(this);
		
		//Hash map for all labels. Labels are not treated as children of shapes.
		this._labels = new Hash();
		
		// create SVG node
		this.node = ORYX.Editor.graft("http://www.w3.org/2000/svg",
			null,
			['g', {id:this.id},
				['g', {"class": "stencils"},
					['g', {"class": "me"}],
					['g', {"class": "children", style:"overflow:hidden"}],
					['g', {"class": "edge"}]
				],
				['g', {"class": "controls"},
					['g', {"class": "dockers"}],
					['g', {"class": "magnets"}]				
				]
			]);
	},

	/**
	 * If changed flag is set, refresh method is called.
	 */
	update: function() {
		//if(this.isChanged) {
			//this.layout();
		//}
	},
	
	/**
	 * !!!Not called from any sub class!!!
	 */
	_update: function() {

	},
	
	/**
	 * Calls the super class refresh method
	 *  and updates the svg elements that are referenced by a property.
	 */
	refresh: function() {
		//call base class refresh method
		arguments.callee.$.refresh.apply(this, arguments);
		
		if(this.node.ownerDocument) {
			//adjust SVG to properties' values
			var me = this;
			this.propertiesChanged.each((function(propChanged) {
				if(propChanged.value) {
					var prop = this.properties[propChanged.key];
					var property = this.getStencil().property(propChanged.key);
					this.propertiesChanged[propChanged.key] = false;

					//handle choice properties
					if(property.type() == ORYX.CONFIG.TYPE_CHOICE) {
						//iterate all references to SVG elements
						property.refToView().each((function(ref) {
							//if property is referencing a label, update the label
							if(ref !== "") {
								var label = this._labels[this.id + ref];
								if (label) {
									label.text(property.item(prop).value());
								}
							}
						}).bind(this));
							
						//if the choice's items are referencing SVG elements
						// show the selected and hide all other referenced SVG
						// elements
						var refreshedSvgElements = new Hash();
						property.items().each((function(item) {
							item.refToView().each((function(itemRef) {
								if(itemRef == "") { this.propertiesChanged[propChanged.key] = true; return; }
								
								var svgElem = this.node.ownerDocument.getElementById(this.id + itemRef);
	
								if(!svgElem) { this.propertiesChanged[propChanged.key] = true; return; }
								
								
								/* Do not refresh the same svg element multiple times */
								if(!refreshedSvgElements[svgElem.id] || prop == item.value()) {
									svgElem.setAttributeNS(null, 'display', ((prop == item.value()) ? 'inherit' : 'none'));
									refreshedSvgElements[svgElem.id] = svgElem;
								}
								
								// Reload the href if there is an image-tag
								if(ORYX.Editor.checkClassType(svgElem, SVGImageElement)) {
									svgElem.setAttributeNS('http://www.w3.org/1999/xlink', 'href', svgElem.getAttributeNS('http://www.w3.org/1999/xlink', 'href'));
								}
							}).bind(this));
						}).bind(this));
						
					} else { //handle properties that are not of type choice
						//iterate all references to SVG elements
						property.refToView().each((function(ref) {
							//if the property does not reference an SVG element,
							// do nothing

							if(ref === "") { this.propertiesChanged[propChanged.key] = true; return; }
		
							var refId = this.id + ref;

							//get the SVG element
							var svgElem = this.node.ownerDocument.getElementById(refId);

							//if the SVG element can not be found
							if(!svgElem || !(svgElem.ownerSVGElement)) { 
								//if the referenced SVG element is a SVGAElement, it cannot
								// be found with getElementById (Firefox bug).
								// this is a work around
								if(property.type() === ORYX.CONFIG.TYPE_URL || property.type() === ORYX.CONFIG.TYPE_DIAGRAM_LINK) {
									var svgElems = this.node.ownerDocument.getElementsByTagNameNS('http://www.w3.org/2000/svg', 'a');
									
									svgElem = $A(svgElems).find(function(elem) {
										return elem.getAttributeNS(null, 'id') === refId;
									});
									
									if(!svgElem) { this.propertiesChanged[propChanged.key] = true; return; } 
								} else {
									this.propertiesChanged[propChanged.key] = true;
									return;
								}					
							}
							
							if (property.complexAttributeToView()) {
								var label = this._labels[refId];
								if (label) {
									try {
								    	propJson = prop.evalJSON();
								    	var value = propJson[property.complexAttributeToView()]
								    	label.text(value ? value : prop);
								    } catch (e) {
								    	label.text(prop);
								    }
								}
								
							} else {

								switch (property.type()) {
									case ORYX.CONFIG.TYPE_BOOLEAN:	
										
										if (typeof prop == "string")
											prop = prop === "true"
	
										svgElem.setAttributeNS(null, 'display', (!(prop === property.inverseBoolean())) ? 'inherit' : 'none');
										
										break;
									case ORYX.CONFIG.TYPE_COLOR:
										if(property.fill()) {
											if (svgElem.tagName.toLowerCase() === "stop"){
												svgElem.setAttributeNS(null, "stop-color", prop);
												
												// Adjust stop color of the others
												if (svgElem.parentNode.tagName.toLowerCase() === "radialgradient"){
													ORYX.Utils.adjustGradient(svgElem.parentNode, svgElem);
												}
											} else {
												svgElem.setAttributeNS(null, 'fill', prop);
											}
										}
										if(property.stroke()) {
											svgElem.setAttributeNS(null, 'stroke', prop);
										}
										break;
									case ORYX.CONFIG.TYPE_STRING:
										var label = this._labels[refId];
										if (label) {
											label.text(prop);
										}
										break;
									case ORYX.CONFIG.TYPE_INTEGER:
										var label = this._labels[refId];
										if (label) {
											label.text(prop);
										}
										break;
									case ORYX.CONFIG.TYPE_FLOAT:
										if(property.fillOpacity()) {
											svgElem.setAttributeNS(null, 'fill-opacity', prop);
										} 
										if(property.strokeOpacity()) {
											svgElem.setAttributeNS(null, 'stroke-opacity', prop);
										}
										if(!property.fillOpacity() && !property.strokeOpacity()) {
											var label = this._labels[refId];
											if (label) {
												label.text(prop);
											}
										}
										break;
									case ORYX.CONFIG.TYPE_URL:
									case ORYX.CONFIG.TYPE_DIAGRAM_LINK:
										//TODO what is the dafault path?
										var hrefAttr = svgElem.getAttributeNodeNS('http://www.w3.org/1999/xlink', 'xlink:href');
										if(hrefAttr) {
											hrefAttr.textContent = prop;
										} else {
											svgElem.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', prop);
										}	
										break;
								}
							}
						}).bind(this));
						
						
					}
					
				}
			}).bind(this));
			
			//update labels
			this._labels.values().each(function(label) {
				label.update();
			});
		}
	},
	
	layout: function() {
		//this.getStencil().layout(this)
		var layoutEvents = this.getStencil().layout()
		if(this instanceof ORYX.Core.Node && layoutEvents) {
			layoutEvents.each(function(event) {
				
				// setup additional attributes
				event.shape = this;
				event.forceExecution = true;
				
				// do layouting
				this._delegateEvent(event);
			}.bind(this))
			
		}
	},
	
	/**
	 * Returns an array of Label objects.
	 */
	getLabels: function() {
		return this._labels.values();
	},

	/**
	 * Returns an array of dockers of this object.
	 */
	getDockers: function() {
		return this.dockers;
	},
	
	getMagnets: function() {
		return this.magnets;
	},
	
	getDefaultMagnet: function() {
		if(this._defaultMagnet) {
			return this._defaultMagnet;
		} else if (this.magnets.length > 0) {
			return this.magnets[0];
		} else {
			return undefined;
		}
	},

	getParentShape: function() {
		return this.parent;
	},
	
	getIncomingShapes: function(iterator) {
		if(iterator) {
			this.incoming.each(iterator);
		}
		return this.incoming;
	},
	
	getIncomingNodes: function(iterator) {
        return this.incoming.select(function(incoming){
            var isNode = (incoming instanceof ORYX.Core.Node);
            if(isNode && iterator) iterator(incoming);
            return isNode;
        });
    },
	
	
	getOutgoingShapes: function(iterator) {
		if(iterator) {
			this.outgoing.each(iterator);
		}
		return this.outgoing;
	},
    
    getOutgoingNodes: function(iterator) {
        return this.outgoing.select(function(out){
            var isNode = (out instanceof ORYX.Core.Node);
            if(isNode && iterator) iterator(out);
            return isNode;
        });
    },
	
	getAllDockedShapes: function(iterator) {
		var result = this.incoming.concat(this.outgoing);
		if(iterator) {
			result.each(iterator);
		}
		return result
	},

	getCanvas: function() {
		if(this.parent instanceof ORYX.Core.Canvas) {
			return this.parent;
		} else if(this.parent instanceof ORYX.Core.Shape) {
			return this.parent.getCanvas();
		} else {
			return undefined;
		}
	},
	
	/**
	 * 
	 * @param {Object} deep
	 * @param {Object} iterator
	 */
	getChildNodes: function(deep, iterator) {
		if(!deep && !iterator) {
			return this.nodes.clone();
		} else {
			var result = [];
			this.nodes.each(function(uiObject) {
				if(!uiObject.isVisible){return}
				if(iterator) {
					iterator(uiObject);
				}
				result.push(uiObject);
				
				if(deep && uiObject instanceof ORYX.Core.Shape) {
					result = result.concat(uiObject.getChildNodes(deep, iterator));
				}
			});
	
			return result;
		}
	},
	
	/**
	 * Overrides the UIObject.add method. Adds uiObject to the correct sub node.
	 * @param {UIObject} uiObject
	 * @param {Number} index
	 */
	add: function(uiObject, index) {
		//parameter has to be an UIObject, but
		// must not be an Edge.
		if(uiObject instanceof ORYX.Core.UIObject 
			&& !(uiObject instanceof ORYX.Core.Edge)) {
			
			if (!(this.children.member(uiObject))) {
				//if uiObject is child of another parent, remove it from that parent.
				if(uiObject.parent) {
					uiObject.parent.remove(uiObject);
				}

				//add uiObject to this Shape
				if(index != undefined)
					this.children.splice(index, 0, uiObject);
				else
					this.children.push(uiObject);

				//set parent reference
				uiObject.parent = this;

				//add uiObject.node to this.node depending on the type of uiObject
				var parent;
				if(uiObject instanceof ORYX.Core.Node) {
					parent = this.node.childNodes[0].childNodes[1];
					this.nodes.push(uiObject);
				} else if(uiObject instanceof ORYX.Core.Controls.Control) {
					var ctrls = this.node.childNodes[1];
					if(uiObject instanceof ORYX.Core.Controls.Docker) {
						parent = ctrls.childNodes[0];
						if (this.dockers.length >= 2){
							this.dockers.splice(index!==undefined?Math.min(index, this.dockers.length-1):this.dockers.length-1, 0, uiObject);
						} else {
							this.dockers.push(uiObject);
						}
					} else if(uiObject instanceof ORYX.Core.Controls.Magnet) {
						parent = ctrls.childNodes[1];
						this.magnets.push(uiObject);
					} else {
						parent = ctrls;
					}
				} else {	//UIObject
					parent = this.node;
				}

				if(index != undefined && index < parent.childNodes.length)
					uiObject.node = parent.insertBefore(uiObject.node, parent.childNodes[index]);
				else
					uiObject.node = parent.appendChild(uiObject.node);
					
				this._changed();
				//uiObject.bounds.registerCallback(this._changedCallback);
				
				
				if(this.eventHandlerCallback)
					this.eventHandlerCallback({type:ORYX.CONFIG.EVENT_SHAPEADDED,shape:uiObject})
					
			} else {

				ORYX.Log.warn("add: ORYX.Core.UIObject is already a child of this object.");
			}
		} else {

			ORYX.Log.warn("add: Parameter is not of type ORYX.Core.UIObject.");
		}
	},

	/**
	 * Overrides the UIObject.remove method. Removes uiObject.
	 * @param {UIObject} uiObject
	 */
	remove: function(uiObject) {
		//if uiObject is a child of this object, remove it.
		if (this.children.member(uiObject)) {
			//remove uiObject from children
			this.children = this.children.without(uiObject);

			//delete parent reference of uiObject
			uiObject.parent = undefined;

			//delete uiObject.node from this.node
			if(uiObject instanceof ORYX.Core.Shape) {
				if(uiObject instanceof ORYX.Core.Edge) {
					uiObject.removeMarkers();
					uiObject.node = this.node.childNodes[0].childNodes[2].removeChild(uiObject.node);
				} else {
					uiObject.node = this.node.childNodes[0].childNodes[1].removeChild(uiObject.node);
					this.nodes = this.nodes.without(uiObject);
				}
			} else if(uiObject instanceof ORYX.Core.Controls.Control) {
				if (uiObject instanceof ORYX.Core.Controls.Docker) {
					uiObject.node = this.node.childNodes[1].childNodes[0].removeChild(uiObject.node);
					this.dockers = this.dockers.without(uiObject);
				} else if (uiObject instanceof ORYX.Core.Controls.Magnet) {
					uiObject.node = this.node.childNodes[1].childNodes[1].removeChild(uiObject.node);
					this.magnets = this.magnets.without(uiObject);
				} else {
					uiObject.node = this.node.childNodes[1].removeChild(uiObject.node);
				}
			}

			this._changed();
			//uiObject.bounds.unregisterCallback(this._changedCallback);
		} else {

			ORYX.Log.warn("remove: ORYX.Core.UIObject is not a child of this object.");
		}
	},
	
	/**
	 * Calculate the Border Intersection Point between two points
	 * @param {PointA}
	 * @param {PointB}
	 */
	getIntersectionPoint: function() {
			
		var pointAX, pointAY, pointBX, pointBY;
		
		// Get the the two Points	
		switch(arguments.length) {
			case 2:
				pointAX = arguments[0].x;
				pointAY = arguments[0].y;
				pointBX = arguments[1].x;
				pointBY = arguments[1].y;
				break;
			case 4:
				pointAX = arguments[0];
				pointAY = arguments[1];
				pointBX = arguments[2];
				pointBY = arguments[3];
				break;
			default:
				throw "getIntersectionPoints needs two or four arguments";
		}
		
		
		
		// Defined an include and exclude point
		var includePointX, includePointY, excludePointX, excludePointY;

		var bounds = this.absoluteBounds();
		
		if(this.isPointIncluded(pointAX, pointAY, bounds)){
			includePointX = pointAX;
			includePointY = pointAY;
		} else {
			excludePointX = pointAX;
			excludePointY = pointAY;
		}

		if(this.isPointIncluded(pointBX, pointBY, bounds)){
			includePointX = pointBX;
			includePointY = pointBY;
		} else {
			excludePointX = pointBX;
			excludePointY = pointBY;
		}
				
		// If there is no inclue or exclude Shape, than return
		if(!includePointX || !includePointY || !excludePointX || !excludePointY) {
			return undefined;
		}

		var midPointX = 0;
		var midPointY = 0;		
		
		var refPointX, refPointY;
		
		var minDifferent = 1;
		// Get the UpperLeft and LowerRight
		//var ul = bounds.upperLeft();
		//var lr = bounds.lowerRight();
		
		var i = 0;
		
		while(true) {
			// Calculate the midpoint of the current to points	
			var midPointX = Math.min(includePointX, excludePointX) + ((Math.max(includePointX, excludePointX) - Math.min(includePointX, excludePointX)) / 2.0);
			var midPointY = Math.min(includePointY, excludePointY) + ((Math.max(includePointY, excludePointY) - Math.min(includePointY, excludePointY)) / 2.0);
			
			
			// Set the new midpoint by the means of the include of the bounds
			if(this.isPointIncluded(midPointX, midPointY, bounds)){
				includePointX = midPointX;
				includePointY = midPointY;
			} else {
				excludePointX = midPointX;
				excludePointY = midPointY;
			}			
			
			// Calc the length of the line
			var length = Math.sqrt(Math.pow(includePointX - excludePointX, 2) + Math.pow(includePointY - excludePointY, 2))
			// Calc a point one step from the include point
			refPointX = includePointX + ((excludePointX - includePointX) / length),
			refPointY = includePointY + ((excludePointY - includePointY) / length)
					
			
			// If the reference point not in the bounds, break
			if(!this.isPointIncluded(refPointX, refPointY, bounds)) {
				break
			}
							
			
		}

		// Return the last includepoint
		return {x:refPointX , y:refPointY};
	},

   
    
    /**
     * Calculate if the point is inside the Shape
     * @param {PointX}
     * @param {PointY} 
     */
    isPointIncluded: function(){
		return  false
	},

    
    /**
     * Calculate if the point is over an special offset area
     * @param {Point}
     */
    isPointOverOffset: function(){
		return  this.isPointIncluded.apply( this , arguments )
	},
		
	_dockerChanged: function() {

	},
		
	/**
	 * Create a Docker for this Edge
	 *
	 */
	createDocker: function(index, position) {
		var docker = new ORYX.Core.Controls.Docker({eventHandlerCallback: this.eventHandlerCallback});
		docker.bounds.registerCallback(this._dockerChangedCallback);
		if(position) {
			docker.bounds.centerMoveTo(position);
		}
		this.add(docker, index);
		
		return docker
	},

	/**
	 * Get the serialized object
	 * return Array with hash-entrees (prefix, name, value)
	 * Following values will given:
	 * 		Bounds
	 * 		Outgoing Shapes
	 * 		Parent
	 */
	serialize: function() {
		var serializedObject = arguments.callee.$.serialize.apply(this);

		// Add the bounds
		serializedObject.push({name: 'bounds', prefix:'oryx', value: this.bounds.serializeForERDF(), type: 'literal'});

		// Add the outgoing shapes
		this.getOutgoingShapes().each((function(followingShape){
			serializedObject.push({name: 'outgoing', prefix:'raziel', value: '#'+ERDF.__stripHashes(followingShape.resourceId), type: 'resource'});			
		}).bind(this));

		// Add the parent shape, if the parent not the canvas
		//if(this.parent instanceof ORYX.Core.Shape){
			serializedObject.push({name: 'parent', prefix:'raziel', value: '#'+ERDF.__stripHashes(this.parent.resourceId), type: 'resource'});	
		//}			
		
		return serializedObject;
	},
		
		
	deserialize: function(serialze){
		arguments.callee.$.deserialize.apply(this, arguments);
		
		// Set the Bounds
		var bounds = serialze.find(function(ser){ return (ser.prefix+"-"+ser.name) == 'oryx-bounds'});
		if(bounds) {
			var b = bounds.value.replace(/,/g, " ").split(" ").without("");
			if(this instanceof ORYX.Core.Edge){
				this.dockers.first().bounds.centerMoveTo(parseFloat(b[0]), parseFloat(b[1]));
				this.dockers.last().bounds.centerMoveTo(parseFloat(b[2]), parseFloat(b[3]));
			} else {
				this.bounds.set(parseFloat(b[0]), parseFloat(b[1]), parseFloat(b[2]), parseFloat(b[3]));
			}			
		}
	},

		
	/**
	 * Private methods.
	 */

	/**
	 * Child classes have to overwrite this method for initializing a loaded
	 * SVG representation.
	 * @param {SVGDocument} svgDocument
	 */
	_init: function(svgDocument) {
		//adjust ids
		this._adjustIds(svgDocument, 0);
	},

	_adjustIds: function(element, idIndex) {
		if(element instanceof Element) {
			var eid = element.getAttributeNS(null, 'id');
			if(eid && eid !== "") {
				element.setAttributeNS(null, 'id', this.id + eid);
			} else {
				element.setAttributeNS(null, 'id', this.id + "_" + this.id + "_" + idIndex);
				idIndex++;
			}
			
			// Replace URL in fill attribute
			var fill = element.getAttributeNS(null, 'fill');
			if (fill&&fill.include("url(#")){
				fill = fill.replace(/url\(#/g, 'url(#'+this.id);
				element.setAttributeNS(null, 'fill', fill);
			}
			
			if(element.hasChildNodes()) {
				for(var i = 0; i < element.childNodes.length; i++) {
					idIndex = this._adjustIds(element.childNodes[i], idIndex);
				}
			}
		}
		return idIndex;
	},

	toString: function() { return "ORYX.Core.Shape " + this.getId() }
};
ORYX.Core.Shape = ORYX.Core.AbstractShape.extend(ORYX.Core.Shape);