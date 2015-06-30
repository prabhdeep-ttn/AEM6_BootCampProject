<%@include file="/libs/foundation/global.jsp"%>
<%@page import="java.util.Collection,java.util.Iterator,com.day.cq.wcm.api.Page" %>
<cq:includeClientLib categories="RSSClientLib"/>

<%int noOfFeeds=Integer.parseInt((String)properties.get("jcr:text","10"));%>
<b><%=noOfFeeds%></b> RSS Feeds are displayed below : <br>

<%String parent = "/content/bootcamp/RSSTestPage"; %>

<input type="hidden" id="url" value="<%=(String)properties.get("servletUrl", "/bin/servlets/RSSservlet")%>">
<input type="hidden" id="parentPath" value="<%=parent%>">
<input type="hidden" id="noOfFeeds" value="<%=noOfFeeds%>">
<input type="hidden" id="page" value='<%=request.getParameter("page")%>'>

<div class="index" id="index"></div>
<hr><div id="RSSFeeds"></div>