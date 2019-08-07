<%@ page language="java" contentType="text/html; charset=utf-8" errorPage="/error.do" %>
<%@ page import="org.agnitas.web.MailingBaseAction"  %>
<%@ taglib prefix="mvc" uri="https://emm.agnitas.de/jsp/jsp/spring" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ACTION_VIEW" value="<%=  MailingBaseAction.ACTION_VIEW %>" scope="page"/>
<c:set var="ACTION_USED_ACTIONS" value="<%=  MailingBaseAction.ACTION_USED_ACTIONS %>" scope="page"/>
<c:set var="ACTION_CONFIRM_DELETE" value="<%=  MailingBaseAction.ACTION_CONFIRM_DELETE %>" scope="page"/>

<%--@elvariable id="unsentMails" type="java.util.List"--%>

<div class="calendar-sidebar-header">
    <mvc:message code="calendar.unscheduledMailings"/>
</div>
<ul class="link-list">
    <c:forEach var="unsentMail" items="${unsentMails}">
        <li>
            <c:url var="mailingBaseUrl" value="/mailingbase.do">
                <c:param name="action" value="${ACTION_VIEW}"/>
                <c:param name="mailingID" value="${unsentMail.mailingid}"/>
            </c:url>
            <a href="${mailingBaseUrl}" class="link-list-item">
                <p class="headline">
                    <span class="mailing-badge ${unsentMail.workstatus}"
                          data-tooltip="<mvc:message code="${unsentMail.workstatus}"/>"></span>
                        ${unsentMail.shortname}
                </p>
                <p class="description">
                    <span data-tooltip="<mvc:message code='birt.mailinglist'/>">
                        <i class="icon icon-list-ul"></i>
                        <span class="text">${unsentMail.mailinglist}</span>
                    </span>
                </p>
            </a>
        </li>
    </c:forEach>
</ul>
