package edu.neu.webapp.graphiccodegen.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
@SessionAttributes({"sessionScriptName", "sessionStatementType", "sessionVariableObjects"})
public class DataController {

	@Autowired
    private DataDao dataDao;
	
	@Autowired
    private NumberOperationDao numberOperationDao;
	
	@Autowired
    private ScriptDao scriptDao;
	
	@Autowired
	private StringOperationDao stringOperationDao;
	
	@Autowired
    private StatementTypeDao statementTypeDao;
	
	@Autowired
    private OperationTypeDao operationTypeDao;
	
	@Autowired
	private StatementDao statementDao;
	
	@Autowired
	private BranchDao branchDao;
	
	
	 @RequestMapping(value="/statementWithValues")
	    public String renderChosenStatement(ModelMap model, HttpServletRequest request) {
		 	
		 	model.put("sessionStatementType", request.getParameter("scriptstmtType"));
		 	
		 	renderPageValues(model);
	        
	        return "scriptstatementpage";
	    }
	
	 @RequestMapping(value="/dataWithValues")
	    public String installData(ModelMap model, HttpServletRequest request) {
	    	
	        String scriptName = String.valueOf(model.get("sessionScriptName"));
	        String scriptStmtType = String.valueOf(model.get("sessionStatementType"));
	        String dataName = request.getParameter("varName");
	        String dataType = request.getParameter("varType");
	        String dataValue = request.getParameter("varValue");

	        dataDao.persist(new Data(statementTypeDao.getStatementType(scriptStmtType), scriptDao.getScript(scriptName), dataName, dataValue, dataType));
	        
	        renderPageValues(model);
	        return "scriptstatementpage";
	    }
	 
	@RequestMapping(value = "/editdatastatement")
	public String editDataStatement(ModelMap model, HttpServletRequest request) {
		
		if (request.getParameter("deleteAction") != null) {

			int oldDataStatement = Integer.valueOf(request.getParameter("deleteAction"));
			dataDao.deleteDataStatementById(oldDataStatement);
			
			renderPageValues(model);
			
			return "scriptstatementpage";
		} else {

			renderPageValues(model);
			return "scriptstatementpage";
		}
	}
	 
	 @RequestMapping(value="/scriptstatementpage")
	public String renderMainStatements(ModelMap model,HttpServletRequest request) {
		 
		renderPageValues(model);
		return "scriptstatementpage";
	}
	 
	 
	 @RequestMapping(value="/displayVariableValues")
	 public String displayDataValues(){
		 
		 try{
	    		String data = "<%@taglib prefix=&quot;form&quot; uri=&quot;http://www.springframework.org/tags/form&quot;%><!DOCTYPE html PUBLIC &quot;-//W3C//DTD HTML 4.01 Transitional//EN&quot; &quot;http://www.w3.org/TR/html4/loose.dtd&quot;><html><head><meta http-equiv=&quot;Content-Type&quot; content=&quot;text/html; charset=ISO-8859-1&quot;></head><body><c:forEach var=&quot;dataStatement&quot; items=&quot;${sessionVariableObjects}&quot;><tableborder=&quot;&quot;><tr><th>Variable</th><th>Value</th></tr><tr><td></td>${dataStatement.getDataName()}<td>${dataStatement.getDataValue()}</td></tr></table></c:forEach></body></html>";
	 
	    		File file = new File("C:/Users/TejaswiniHegde/Documents/GitHub/webbased-visual-codegen/graphicalcodegen/graphic-code-gen/src/main/webapp/WEB-INF/displayFinalValues.jsp");
	    		//File file = new File("displayFinalValues.jsp");
	    		//if file doesnt exists, then create it
	    		if(!file.exists()){
	    			file.createNewFile();
	    			System.out.println("File Created");
	    		}
	 
	    		//true = append file
	    		FileWriter fileWritter = new FileWriter(file.getAbsoluteFile());
	    			//FileWriter fileWritter = new FileWriter(file.getName(),true);
	    	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
	    	        bufferWritter.write(data);
	    	        bufferWritter.close();
	 
		        System.out.println("File Written to "+file.getAbsolutePath());
		        
		      
		        
	 
	    	}catch(IOException e){
	    		e.printStackTrace();
	    	}
	 return "displayFinalValues";
	 }
	 
	 
	 @RequestMapping(value="/displayFinalValues")
	 public String displayFinalValues(){
		 
		 return "displayFinalValues";
	 }
	 
	 public void renderPageValues(ModelMap model){
		 
			List<StatementType> stmtTypes = statementTypeDao.getAllStatementTypes();
			model.put("statementTypes", stmtTypes);

			String scriptName = String.valueOf(model.get("sessionScriptName"));
			 
			List<Data> dataStatements = dataDao.getAllDataStatements(scriptName);
			model.put("sessionVariableObjects", dataStatements);
			
			List<Branch> branchStatements = branchDao.getAllBranchStatements(scriptName);
			model.put("branchStatements", branchStatements);
			
			List<OperationType> operationTypes = operationTypeDao.getAllOperationTypes();
	        model.put("operationTypes", operationTypes);
			
		 	List<NumberOperation> numberOperations = numberOperationDao.getAllNumberOperationStatements(scriptName);
		 	model.put("numberOperations", numberOperations);
		 	
		 	List<StringOperation> stringOperations = stringOperationDao.getAllStringOperationStatements(scriptName);
			model.put("stringOperations", stringOperations);
			
			List<Statement> statements = statementDao.getAllStatements(scriptName);
			model.put("statements", statements);
					
	 }
}
