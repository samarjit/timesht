SET DEFINE OFF;
delete from fw_menu;

Insert into FW_MENU
   (MENU_ID, MENU_NAME, MENU_ROLE_ID, MENU_ACTION)
 Values
   (1, 'Request', 'ADMIN', 'template1.action?screenName=frmRequestList');
Insert into FW_MENU
   (MENU_ID, MENU_NAME, MENU_ROLE_ID, MENU_ACTION)
 Values
   (6, 'Delivery Note', 'ADMIN', 'template1.action?screenName=frmDeliveryNoteList');
Insert into FW_MENU
   (MENU_ID, MENU_NAME, MENU_ROLE_ID, MENU_ACTION)
 Values
   (3, 'Quotation', 'ADMIN', 'template1.action?screenName=frmQuotationList');
Insert into FW_MENU
   (MENU_ID, MENU_NAME, MENU_ROLE_ID, MENU_ACTION)
 Values
   (4, 'RRF', 'ADMIN', 'template1.action?screenName=frmRRFList');
Insert into FW_MENU
   (MENU_ID, MENU_NAME, MENU_ROLE_ID, MENU_ACTION)
 Values
   (13, 'View Workflow', 'ADMIN', 'viewworkflow.jsp');
Insert into FW_MENU
   (MENU_ID, MENU_NAME, MENU_ROLE_ID, MENU_ACTION)
 Values
   (8, 'Request', 'MANAGER', 'template1.action?screenName=frmRequestList');
Insert into FW_MENU
   (MENU_ID, MENU_NAME, MENU_ROLE_ID, MENU_ACTION)
 Values
   (2, 'RFQ', 'ADMIN', 'template1.action?screenName=frmRFQList');
Insert into FW_MENU
   (MENU_ID, MENU_NAME, MENU_ROLE_ID, MENU_ACTION)
 Values
   (5, 'Purchase Order', 'ADMIN', 'template1.action?screenName=frmPOList');
Insert into FW_MENU
   (MENU_ID, MENU_NAME, MENU_ROLE_ID, MENU_ACTION)
 Values
   (10, 'Allocation', 'ADMIN', 'template1.action?screenName=frmAllocationList');
Insert into FW_MENU
   (MENU_ID, MENU_NAME, MENU_ROLE_ID, MENU_ACTION)
 Values
   (11, 'Request for Approval', 'MANAGER', 'template1.action?screenName=frmApprovalList');
Insert into FW_MENU
   (MENU_ID, MENU_NAME, MENU_ROLE_ID, MENU_ACTION)
 Values
   (12, 'RRF for Approval', 'MANAGER', 'template1.action?screenName=frmApprovalRRFList');
Insert into FW_MENU
   (MENU_ID, MENU_NAME, MENU_ROLE_ID, MENU_ACTION)
 Values
   (7, 'Inventory', 'ADMIN', 'template1.action?screenName=frmAssetList');
Insert into FW_MENU
   (MENU_ID, MENU_NAME, MENU_ROLE_ID, MENU_ACTION)
 Values
   (14, 'Create Timesheet', 'ADMIN', 'template1.action?screenName=frmTSheetList');
Insert into FW_MENU
   (MENU_ID, MENU_NAME, MENU_ROLE_ID, MENU_ACTION)
 Values
   (15, 'Create Screen', 'ADMIN', 'pages/createscreen.jsp');
Insert into FW_MENU
   (MENU_ID, MENU_NAME, MENU_ROLE_ID, MENU_ACTION)
 Values
   (16, 'Task Master', 'ADMIN', 'template1.action?screenName=frmTaskList');
Insert into FW_MENU
   (MENU_ID, MENU_NAME, MENU_ROLE_ID, MENU_ACTION)
 Values
   (17, 'Timesheet', 'ADMIN', 'pages/timesheet.jsp');
Insert into FW_MENU
   (MENU_ID, MENU_NAME, MENU_ROLE_ID, MENU_ACTION)
 Values
   (18, 'MenuEdit', 'ADMIN', 'pages/enginemenu.jsp');
Insert into FW_MENU
   (MENU_ID, MENU_NAME, MENU_ROLE_ID, MENU_ACTION)
 Values
   (9, 'Request', 'USER', 'template1.action?screenName=frmRequestList');
COMMIT;
