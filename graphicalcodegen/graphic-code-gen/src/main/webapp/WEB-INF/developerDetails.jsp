<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
 
<html>
    <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Developer List</title>
        <script src="js/jquery-min.js"></script>
<script src="js/purl.js"></script>
        <script>
        	var entityName, firstName, lastName, entityId;
        	var applicationSelect, applicationOptionTemplate;
        	var applicationList, applicationListItemTemplate, applicationDetailsLink;
        	$(function(){
        		var url = $.url();
        		var id = url.param("entityId");
        		updateEntityDetails(id);
        		
        		applicationSelect = $("#applicationSelect");
        		applicationOptionTemplate = $("#applicationSelect option").clone();
        		
        		applicationList = $("#applicationList");
        		applicationListItemTemplate = $("#applicationList li").clone();
        		
        		entityName = $("#entityName");
        		firstName = $("#firstName");
        		lastName = $("#lastName");
        		entityId = $("#entityId");
        		entityEmail= $("#entityEmail");
        		
        		$("#update").click(updateEntity);
        		$("#delete").click(deleteEntity);
        		//$("#addApplication").click(addApplication);
        		
        		//getAllApplication(renderApplicationOptions);
        	});
        	

        	function updateEntityDetails(id) {
        		$.ajax({
        			"url" : "rest/developer/"+id,
        			"success" : function(entity) {
        				firstName.val(entity.firstName);
        				lastName.val(entity.lastName);
        				//email.val(entity.email);
        				$("#entityEmailShow").html(entity.email);
        				//entityEmail.data=entity.email;
        				entityEmail.val(entity.email);
        			}
        		});
        	}
        	
        	function updateEntity() {
        		alert('update entity'+$("#entityEmail").val());
        		var entity = {
           			"firstName"	: firstName.val(),
           			"lastName"	: lastName.val(),
        			"email"	: $("#entityEmail").val()
        		};
        		$.ajax({
        			"url" : "rest/developer",
        			"data" : JSON.stringify(entity),
        		    "contentType" : "application/json; charset=utf-8",
        	        "dataType" : "json",
        			"type" : "POST",
        			"success" : function(entities) {
        				console.log(entities);
        			}
        		});
        	}
        	
        	function deleteEntity() {
        		var id = entityEmail.val();
        		$.ajax({
        			"url" : "rest/developer/"+id,
        			"type" : "DELETE",
        			"success" : function(entities) {
        				window.location.href = "developerAjax.html";
        			},
        			"error" : function(err) {
        				console.log(err);
        			}
        		});
        	}
        </script>
	</head>
	<body>
    	<a href="applicationList.html">Applications</a>
    	<a href="developer.html">Developers</a>

		<h1>Developer Details</h1>

		<h2>Developer</h2>

		First Name: <input id="firstName"/><br/>
		Last Name: <input id="lastName"/><br/>
		Email: <span id="entityEmailShow"></span><br/>
		<input type="hidden" id="entityEmail"/>
		<a href="#" id="update">Update</a> <a href="#" id="delete">Delete</a>

		<hr/>


	</body>
	</html>