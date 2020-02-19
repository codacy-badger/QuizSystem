<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<script type="text/javascript" src="resources/js/datatablesUtil.js" defer></script>
<script type="text/javascript" src="resources/js/myQuestDatatables.js" defer></script>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="jumbotron">
    <div class="container">
        <div class="shadow">
            <h3><spring:message code="quests.my.title"/></h3>

            <div class="form-info"><spring:message code="quests.my.info"/></div>
            <div class="view-box">
                <table class="respondent table-striped" id="datatable">
                    <thead>
                    <tr>
                        <th><spring:message code="quests.my.statusCompleted"/></th>
                        <th><spring:message code="quests.admin.name"/></th>
                        <th><spring:message code="quests.my.doQuest"/></th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
