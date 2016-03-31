<script type="text/javascript">
var $j = jQuery.noConflict();
$j(document).ready(function () {
    sList = $j('input[name="selectedReportList"]:checked');
    find = $j('#findButton');
    results = $j('#results');
    clearResults = $j('a#clearResults');

    firstTime();

    function firstTime() {
        sendRequest();
    }

    function sendRequest() {
        sList = $j('input[name="selectedReportList"]:checked').val();
        $j('#errorSpan').html('');
        $j.get('portlets/reportSearch.portlet', {
                selectedReportList: sList
            },
            function (data) {
                if (data.match(/crossDate/ig) != null) {
                    $j('#errorSpan').html($j(data));
                } else {
                    results.html(data);
                    $j('table#matchedReports').dataTable({

                    });
                }
            });
    }

    // ***********Events*************
    $j("#reportList input[name='selectedReportList']").click(function() {
        sendRequest();
    });

    pQuery.keypress(function (event) {
        if (event.which == '13') {
            sendRequest();
        }
    });

    clearResults.click(function () {
        $j('table#searchForm input:text').val('');
        $j('table#searchForm input[type="checkbox"]').attr('checked', false);
        $j('tbody#matchedOrdersBody').html('');
    });

    $j('input#voidOrderButton').click(function () {
        // TODO

    });

    // ************Popups***************
    $j('#voidReasonPopup').dialog({
        autoOpen: false,
        modal: true,
        position: top,
        title: '<spring:message code="radiology.voidReason" javaScriptEscape="true"/>',
        width: '40%'
    });
});
</script>
