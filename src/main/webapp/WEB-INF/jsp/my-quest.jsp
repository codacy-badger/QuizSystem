<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<script type="text/javascript" src="resources/js/myQuestUtil.js" defer></script>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="jumbotron">
    <div class="container">
        <div class="shadow">
            <h3 id="questName"></h3>
            <form:form id="result" data-toggle="validator" role="form">
                <div class="view-box">
                    <input type="hidden" name="id" id="resultId">
                    <input type="hidden" name="status" id="status" value="UNCOMPLETED">
                    <input type="hidden" name="quest[id]" id="questId">
                    <div class="form-questions" id="questions">

                    </div>

                    <a class="btn btn-success" onclick="finishResult()">
                        <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
                        <spring:message code="quest.my.finish"/>
                    </a>

                    <a class="btn btn-info" onclick="saveResult()">
                        <span class="glyphicon glyphicon-pause" aria-hidden="true"></span>
                        <spring:message code="quest.my.save-and-pause"/>
                    </a>
                </div>
            </form:form>
        </div>
    </div>
</div>


<div class="template-question" style="display: none;">
    <div class="row" id="questionNumber'{number}'">
        <div class="col-sm-12">
            <div class="panel panel-default">
                <div class="panel-body">
                    <div class="form-horizontal">
                        <div class="form-survey">
                            <input type="hidden" name="answers['{number}'][id]" id="answerId'{number}'" answerQuestionId="">
                            <input type="hidden" name="answers['{number}'][question][id]" id="questionId'{number}'">
                            <input type="hidden" name="answers['{number}'][question][answerTypeId]" id="answerTypeId'{number}'">
                                <label class="label-question h4"
                                        name="questions['{number}'][name]" id="questionName'{number}'">
                                </label>
                        </div>
                        <div class="form-answers form-group">
                            <div class="help-block with-errors form-survey-content"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="template-answers-variants" style="display: none;">
    <div class="form-survey-content" id="question'{questionNumber}'VariantNumber'{number}'">
        <div class="pretty p-default p-round p-thick form-survey-content">
            <input required type="radio" name="answers['{questionNumber}'][variant][id]" id="question'{questionNumber}'VariantId'{number}'" value="" questionId="">
            <div class="state p-primary-o">
                <label class="label-answer form-check-label" id="question'{questionNumber}'VariantName'{number}'" for="question'{questionNumber}'VariantId'{number}'"></label>
            </div>
        </div>
    </div>
</div>

<div class="template-answers-text" style="display: none;">
    <div class="form-survey-content">
        <div class="col-sm-12">
            <label class="label-answer control-label" for="question'{questionNumber}'AnswerText"></label>
            <textarea required class="form-control textarea-answer" rows="3" name="answers['{questionNumber}'][answerText]" id="question'{questionNumber}'AnswerText" questionId=""></textarea>
        </div>
    </div>
</div>

<div class="modal fade" id="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title" id="modalTitleDialog"></h3>
            </div>
            <div class="modal-body">
                <p>></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="">
                    <span class="fa fa-check"></span>
                    <spring:message code="common.close"/>
                </button>
            </div>
        </div>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
