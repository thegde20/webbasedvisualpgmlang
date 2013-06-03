package edu.neu.webapp.graphiccodegen.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import edu.neu.webapp.graphiccodegen.dao.BranchDao;
import edu.neu.webapp.graphiccodegen.dao.DataDao;
import edu.neu.webapp.graphiccodegen.dao.NumberOperationDao;
import edu.neu.webapp.graphiccodegen.dao.OperationTypeDao;
import edu.neu.webapp.graphiccodegen.dao.ScriptDao;
import edu.neu.webapp.graphiccodegen.dao.StatementDao;
import edu.neu.webapp.graphiccodegen.dao.StatementTypeDao;
import edu.neu.webapp.graphiccodegen.dao.StringOperationDao;
import edu.neu.webapp.graphiccodegen.entities.Branch;
import edu.neu.webapp.graphiccodegen.entities.Data;
import edu.neu.webapp.graphiccodegen.entities.NumberOperation;
import edu.neu.webapp.graphiccodegen.entities.OperationType;
import edu.neu.webapp.graphiccodegen.entities.Statement;
import edu.neu.webapp.graphiccodegen.entities.StatementType;
import edu.neu.webapp.graphiccodegen.entities.StringOperation;

@Controller
@SessionAttributes({"sessionScriptName", "sessionStatementType", "sessionOperationType", "sessionVariableObjects"})
public class OperationController {
	
	@Autowired
    private DataDao dataDao;

	@Autowired
    private ScriptDao scriptDao;
	
	@Autowired
	private StringOperationDao stringOperationDao;
	
	@Autowired
    private NumberOperationDao numberOperationDao;
	
	@Autowired
    private StatementTypeDao statementTypeDao;
	
	@Autowired
    private OperationTypeDao operationTypeDao;
	
	@Autowired
	private StatementDao statementDao;
	
	@Autowired
	private BranchDao branchDao;
	
	 @RequestMapping(value="/operationWithValues")
	    public String installOperation(ModelMap model, HttpServletRequest request) {
		 
		 System.out.println("Inside operation Ctrller");
	        String scriptName = String.valueOf(model.get("sessionScriptName"));
	        String scriptStmtType = String.valueOf(model.get("sessionStatementType"));
	        String operationType = String.valueOf(model.get("sessionOperationType"));
	        System.out.println("operationType "+operationType);
	        
		if(operationType.equalsIgnoreCase("Unary")) {

			int dataStmtId = Integer.parseInt(request.getParameter("unaryData1"));
			String operator = request.getParameter("unaryOperator");
			
			numberOperationDao.persist(new NumberOperation(statementTypeDao.getStatementType(scriptStmtType), scriptDao.getScript(scriptName), 
					operationTypeDao.getOperationType(operationType), dataDao.getData(dataStmtId), dataDao.getData(0), dataDao.getData(0), 
					operator, dataDao.getData(0), null));
			
		}else if(operationType.equalsIgnoreCase("Binary")){
			
			System.out.println("Inside Binary");
			int resultId = Integer.parseInt(request.getParameter("result"));
			int dataStmtId1 = Integer.parseInt(request.getParameter("binaryData1"));
			int dataStmtId2 = Integer.parseInt(request.getParameter("binaryData2"));
			String operator = request.getParameter("binaryOperator");
			
			dataDao.updateDataById(resultId, dataStmtId1, dataStmtId2, operator);
			
			System.out.println("Back");
			
			numberOperationDao.persist(new NumberOperation(statementTypeDao.getStatementType(scriptStmtType), scriptDao.getScript(scriptName), 
					operationTypeDao.getOperationType(operationType), dataDao.getData(dataStmtId1), dataDao.getData(dataStmtId2), dataDao.getData(resultId), 
					operator, dataDao.getData(0), null));
			
		}else  if(operationType.equalsIgnoreCase("Decision")){
			
			int resultId = Integer.parseInt(request.getParameter("result"));
			int dataStmtId1 = Integer.parseInt(request.getParameter("decisionData1"));
			int dataStmtId2 = Integer.parseInt(request.getParameter("decisionData2"));
			int dataStmtId3 = Integer.parseInt(request.getParameter("decisionData3"));
			String operator1 = "?";
			String operator2 = ":";
			
			numberOperationDao.persist(new NumberOperation(statementTypeDao.getStatementType(scriptStmtType), scriptDao.getScript(scriptName), 
					operationTypeDao.getOperationType(operationType), dataDao.getData(dataStmtId1), dataDao.getData(dataStmtId2), dataDao.getData(resultId), 
					operator1, dataDao.getData(dataStmtId3), operator2));
			
		}else if(operationType.equalsIgnoreCase("Substring")){
			
			int resultId = Integer.parseInt(request.getParameter("substringResult"));
			int dataStmtId1 = Integer.parseInt(request.getParameter("substringData1"));
			String operator = "substring";
			int startIndex = Integer.parseInt(request.getParameter("startIndex"));
			int endIndex = Integer.parseInt(request.getParameter("endIndex"));
			
			stringOperationDao.persist(new StringOperation(statementTypeDao.getStatementType(scriptStmtType), scriptDao.getScript(scriptName), 
					operationTypeDao.getOperationType(operationType), dataDao.getData(dataStmtId1), dataDao.getData(0), dataDao.getData(resultId), 
					operator, startIndex, endIndex));
		}else if(operationType.equalsIgnoreCase("Concat")){
			
			int resultId = Integer.parseInt(request.getParameter("concatResult"));
			int dataStmtId1 = Integer.parseInt(request.getParameter("concatData1"));
			int dataStmtId2 = Integer.parseInt(request.getParameter("concatData2"));
			String operator = "concat";
			
			stringOperationDao.persist(new StringOperation(statementTypeDao.getStatementType(scriptStmtType), scriptDao.getScript(scriptName), 
					operationTypeDao.getOperationType(operationType), dataDao.getData(dataStmtId1), dataDao.getData(dataStmtId2), dataDao.getData(resultId), 
					operator, 0, 0));
		}

		renderPageValues(model);
		return "scriptstatementpage";
	}

	@RequestMapping(value="/renderoperation")
	    public String renderChosenOperation(ModelMap model, HttpServletRequest request) {
	
		 	model.put("sessionOperationType",request.getParameter("oType"));
		 	
		 	renderPageValues(model);
	        
	        return "scriptstatementpage";
	    }
	 
	@RequestMapping(value = "/editnumberoperation")
	public String editOperationStatement(ModelMap model, HttpServletRequest request) {
		
		if (request.getParameter("deleteAction") != null) {

			int numberOperationStatementId = Integer.valueOf(request.getParameter("deleteAction"));
			numberOperationDao.deleteNumberOperationById(numberOperationStatementId);
			
			renderPageValues(model);
			
			return "scriptstatementpage";
		} else {

			renderPageValues(model);
			return "scriptstatementpage";
		}
	}
	
	@RequestMapping(value = "/editstringoperation")
	public String editStringOperation(ModelMap model, HttpServletRequest request) {
		
		if (request.getParameter("deleteAction") != null) {

			int oldStringOperation = Integer.valueOf(request.getParameter("deleteAction"));
			stringOperationDao.deleteStringOperationStatementById(oldStringOperation);
			
			renderPageValues(model);
			
			return "scriptstatementpage";
		} else {

			renderPageValues(model);
			return "scriptstatementpage";
		}
	}

	 public void renderPageValues(ModelMap model){
		 
			String scriptName = String.valueOf(model.get("sessionScriptName"));
			
		 	List<NumberOperation> numberOperations = numberOperationDao.getAllNumberOperationStatements(scriptName);
		 	model.put("numberOperations", numberOperations);
		 	
			List<OperationType> operationTypes = operationTypeDao.getAllOperationTypes();
	        model.put("operationTypes", operationTypes);
		 
			List<StatementType> stmtTypes = statementTypeDao.getAllStatementTypes();
			model.put("statementTypes", stmtTypes);
			
			List<StringOperation> stringOperations = stringOperationDao.getAllStringOperationStatements(scriptName);
			model.put("stringOperations", stringOperations);

			List<Data> dataStatements = dataDao.getAllDataStatements(scriptName);
			model.put("sessionVariableObjects", dataStatements);
		
			List<Branch> branchStatements = branchDao.getAllBranchStatements(scriptName);
			model.put("branchStatements", branchStatements);
			
			List<Statement> statements = statementDao.getAllStatements(scriptName);
			model.put("statements", statements);
	 }

}
