<#assign ww=JspTaglibs["/WEB-INF/webwork.tld"] >
<#include "/lib/web/macro-lib.ftl"/>
<#include "./macro-lib.ftl"/>
<#assign thisFeedUrl = feed.bookmark.link.url/>
<#assign thisFeedName = feed.bookmark.title/>
<#assign title="edit link collect: " + thisFeedUrl/>
<#assign username=loggedInUser.username/>
<#assign thisPageHref = gzUserFeedUrl(loggedInUser.username,thisFeedUrl)/>
<@pageBegin pageTitle=title cssHref=[gzUrl("/css/gnizr-feed.css")]>
</@pageBegin>
<@headerBlock/>
<@pageContent>
<#assign bct = settingsBCT(username) + 
               [gzBCTPair("edit link collect",gzUserFeedUrl(username,""))]/>
<@infoBlock bct=bct/>
<@pageTitle>Edit Link Collect</@pageTitle>
<@pageDescription>
<p>Change how you want to create bookmarks from this Web feed.
You can save bookmarks into one or more folders, and define 
tags to be added to those bookmarks.</p>
<p>
To stop collecting links from this feed, disable the <b>Auto Import</b>
option.
</p> 
<#if serviceEnabled == false>
<div class="system-message"><b>Annoucement</b>: Sorry! The Link Collect service is currently unavaible. The administrator
asked all robots to a break. But, you can still create or edit 
your Link Collect subscriptions.</div>
</#if>
</@pageDescription>
<div id="feed">
<p id="feed-heading">
<span id="feed-title">${thisFeedName}</span>
<span id="feed-url">${prettyFormatUrl(thisFeedUrl)}</span>
<#assign edtbmUrl=gzUrl("/edtpost?url="+thisFeedUrl?url+"&redirectToPage="+thisPageHref?url) />
<span id="edit-feed-bmark"><a href="${edtbmUrl}">edit description</a></span>
</p>
<h3>Current Settings</h3>
<table id="feed-settings">
<tr><td class="feed-setting-option">Is auto import enabled?</td><td class="feed-setting-value">${mapToYesNo(feed.autoImport)}</td></tr>
<tr><td class="feed-setting-option">Where will bookmarks be saved to?</td>
<td class="feed-setting-value">
<ul>
<#if currentImportFolders?has_content>
<#list currentImportFolders as f>
<#assign rmImFdrHref=gzUrl("/settings/feeds/removeFolder.action?feedUrl="+thisFeedUrl?url+"&importFolders="+f.name?url)/>
<li>${f.name} <a href="${rmImFdrHref}" title="remove from this list">x</a></li>
</#list>
<#else>
<li>My RSS Imported</li>
</#if>
</ul>
</td>
</tr>
<tr><td class="feed-setting-option">What tags will be used to label saved bookmarks?</td>
<td class="feed-setting-value">
<ul>
<#list currentSelectedTags as t>
<#assign rmTagHref=gzUrl("/settings/feeds/removeTag.action?feedUrl="+thisFeedUrl?url+"&tag="+t?url)/>
<li>${t} <a href="${rmTagHref}" title="remove from this list">x</a></li>
</#list>
</ul>
</td>
</tr>
</table>

<h3>Change Settings</h3>
<@ww.form id="edit-feed-form" action="save" namespace="/settings/feeds">
  <@ww.checkbox label="Enable auto import" labelposition="left" name="autoImport" value="${(feed.autoImport?string)?if_exists}"/>
  <@ww.combobox label="Add folder" name="importFolders" list="folders" value=""/>  
  <@ww.combobox label="Add tag" name="tag" list="myTags" value=""/>
  <@ww.hidden name="feedUrl" value="${thisFeedUrl}"/>
  <@ww.submit cssClass="btn" value="save"/>

<#if (action.actionMessages)?has_content>
   <ul class="formErrors">
   <#list (action.actionMessages) as msg>
     <li class="errorMessage">${getActionMsg(msg)}</li>
   </#list>
   </ul>
  </#if> 
</@ww.form>
</div>

<@sidebarBlock>
</@sidebarBlock> 

</@pageContent>
<@pageEnd/>

