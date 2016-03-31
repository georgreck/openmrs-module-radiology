<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="View Orders" otherwise="/login.htm"
	redirect="/module/radiology/radiologyReportList.jsp" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="./localHeader.jsp"%>

<openmrs:htmlInclude file="/moduleResources/radiology/radiology.css" />
<openmrs:htmlInclude
	file="/scripts/jquery-ui/js/jquery-ui-1.7.2.custom.min.js" />
<openmrs:htmlInclude
	file="/moduleResources/radiology/css/jquery.dataTables.min.css" />
<openmrs:htmlInclude
	file="/moduleResources/radiology/js/jquery.dataTables.min.js" />
<%@ include
	file="/WEB-INF/view/module/radiology/resources/js/radiologyReportList.js"%>

<h2>
	<spring:message code="radiology.manageReports" />
</h2>

<span class="boxHeader"> <b> <spring:message
			code="radiology.radiologyReportList" /></b> <a id="clearResults"
	href="#" style="float: right"><spring:message
			code="radiology.clearResults" /></a>
</span>
<div class="box">
	<table id="searchForm" cellspacing="10">
		<tr>
			<td>
				<form id="reportList">
					<input name="selectedReportList" type="radio" value="allReports"
						checked>
					<spring:message code="radiology.radiologyReportShowAll" />
					</input> <input name="selectedReportList" type="radio"
						value="claimedReports">
					<spring:message code="radiology.radiologyReportShowClaimed" />
					</input> <input name="selectedReportList" type="radio"
						value="discontinuedReports">
					<spring:message code="radiology.radiologyReportShowDiscontinued" />
					</input> <input name="selectedReportList" type="radio"
						value="completedReports">
					<spring:message code="radiology.radiologyReportShowCompleted" />
					</input>
				</form>
			</td>
		</tr>
	</table>
	<div id="results"></div>
</div>
<br />
<%@ include file="/WEB-INF/template/footer.jsp"%>
