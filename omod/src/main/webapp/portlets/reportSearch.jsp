<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="View Orders" otherwise="/login.htm"
	redirect="/module/radiology/radioloyReportList.jsp" />

<table id="matchedReports" cellspacing="0" width="100%"
	class="display nowrap">
	<thead>
		<tr>
			<th><spring:message code="radiology.radiologyReportId" /></th>
			<th><spring:message code="radiology.orderId" /></th>
			<th><spring:message code="radiology.patientFullName" /></th>
			<th><spring:message code="radiology.reportStatus" /></th>
			<th><spring:message code="radiology.radiologyReportProvider" /></th>
			<th><spring:message code="radiology.radiologyReportDate" /></th>
		</tr>
	</thead>
	<tbody id="matchedReportsBody">
		<c:forEach items="${reportList}" var="report">
			<tr>
				<td><a
					href="radiologyReport.form?radiologyReportId=${report.id}">${report.id}</a></td>
				<td><a
					href="radiologyOrder.form?orderId=${report.radiologyOrder.orderId}">${report.radiologyOrder.orderId}</a></td>
				<td>${report.radiologyOrder.patient.personName}</td>
				<td>${report.reportStatus}</td>
				<td>${report.principalResultsInterpreter.name}</td>
				<td>${report.reportDate}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
