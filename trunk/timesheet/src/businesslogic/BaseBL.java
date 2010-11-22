package businesslogic;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This Interface comprises of functions that are to be called at various CRUD operations stages. 
 * All the methods take an HashMap by convention request parameter Map and UserDTO is passed
 * to each method 
 * 
 */
public interface BaseBL extends Serializable {
/**
 * This is invoked in screenFlowControllerServlet class while login
 * @param buslogHm
 * @return HashMap result
 */
HashMap  processRequest(Map buslogHm);

/**
 * This is invoked in WorkflowAC class before processing
 * @param buslogHm
 * @return HashMap result
 */
HashMap preSubmitProcessBL(Map hm);

/**
 * This is invoked in WorkflowAC class after processing
 * @param buslogHm
 * @return HashMap result
 */
HashMap postSubmitProcessBL(Map hm);

/**
 * This is invoked in RetrieveDetailsAC class before processing
 * @param buslogHm
 * @return HashMap result
 */
HashMap preRetreiveProcessBL(Map buslogHm);

/**
 * This is invoked in RetrieveDetailsAC class after processing
 * @param buslogHm
 * @return HashMap result
 */
HashMap postRetreiveProcessBL(Map buslogHm);

/**
 * This is invoked in InsertDataAC class after processing
 * @param buslogHm
 * @return HashMap result
 */
HashMap postInsertProcessBL(Map buslogHm);

/**
 * This is invoked in InsertDataAC class before processing
 * @param buslogHm
 * @return HashMap result
 */
HashMap preInsertProcessBL(Map buslogHm);

/**
 * This is invoked in DeleteDataAC class before processing
 * @param buslogHm
 * @return HashMap result
 */
HashMap preDeleteProcessBL(Map buslogHm);

/**
 * This is invoked in DeleteDataAC class after processing
 * @param buslogHm
 * @return HashMap result
 */
HashMap postDeleteProcessBL(Map buslogHm);

/**
 * This is invoked in UpdateDataAC class before processing
 * @param buslogHm
 * @return HashMap result
 */
HashMap preUpdateProcessBL(Map buslogHm);

/**
 * This is invoked in UpdateDataAC class after processing
 * @param buslogHm
 * @return HashMap result
 */
HashMap postUpdateProcessBL(Map buslogHm);

/**
 * This is invoked in JavaScriptRPCAC class  from javascript directly
 * @param buslogHm
 * @return HashMap result
 */
HashMap jsrpcProcessBL(Map buslogHm, String rpcid);
 

}
