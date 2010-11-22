package pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dao.VendorMapDAO;

/**
 * Vendor Map is a pojo class containing the business logic of mapping vendors to existing RFQs
 * @author SAMARJIT
 *
 */
public class VendorMap {
	/**
	 * Priority based logging is handled here where priority 0 means lowest and 5 is considered highest. Class 
	 * level priority setting has been implemented to make testing easy on the basis of layers
	 * @param priority
	 * @param s
	 */
	public void debug(int priority, String s){
		s="VendorMap:"+s;
		if(priority >0)
		System.out.println(s);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * Inserts a new mapping entry in AMS_RFQ_VENDOR_MAP
	 * @param rfqid
	 * @param vendorid
	 * @param typenotify
	 * @param suggestdelvtime
	 * @return
	 */
	public String insert(String rfqid, String vendorid, String typenotify, String suggestdelvtime) {
		VendorMapDAO vdao = new VendorMapDAO();
		return vdao.insert(rfqid,vendorid,typenotify,suggestdelvtime);
		 
	}

	/**
	 * Selects Vendors that can be mapped to RFQs related to a particular department
	 * @param department
	 * @return
	 */
	public Map getVendorList(String department) {
		VendorMapDAO vdao = new VendorMapDAO();
		return vdao.getVendorList(department);
	}

	/**
	 * Selects all the entries of a mapping between RFQ and vendors that are currently there. It also
	 * gets type of notify information and email and print status
	 * @param rfqid
	 * @return
	 */
	public ArrayList<HashMap<String, String>> setlectAll(String rfqid) {
		VendorMapDAO vdao = new VendorMapDAO();
		return vdao.selectAll(rfqid);
	}

	/**
	 * deletes a vendor mapping
	 * @param rfqid
	 * @param vendorid
	 * @return
	 */
	public String delete(String rfqid, String vendorid) {
		VendorMapDAO vdao = new VendorMapDAO();
		return vdao.delete(rfqid, vendorid);
	}

	/**
	 * Maps all the vendors that are possible to map to RFQ of a particular department. User can delete the unwanted 
	 * mappings after this
	 * @param rfqid
	 * @param department
	 * @param typenotify
	 * @param suggestdelvtime
	 * @return
	 */
	public String initialMap(String rfqid, String department, String typenotify, String suggestdelvtime) {
		VendorMapDAO vdao = new VendorMapDAO();
		return vdao.initialMap(rfqid,department,typenotify,suggestdelvtime);
	}

	/**
	 * Type of Notification is updated like email or print and number of times the vendor has been notified.
	 * The information is kept is a textual format so that later new modes of notification eg.SMS can be added 
	 * without any change in java code
	 * @param rfqid
	 * @param vendorid
	 * @param typenotify
	 * @param indvstatus
	 * @return
	 */
	public String updateTypeNotify(String rfqid, String vendorid,
			String typenotify , String indvstatus) {
		VendorMapDAO vdao = new VendorMapDAO();
		return vdao.updateTypeNotify(rfqid,vendorid,typenotify, indvstatus);
	}

}
