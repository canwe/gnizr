<#include "/lib/web/macro-lib.ftl"/>
<#include "./macro-lib.ftl"/>
<#assign username = loggedInUser.username/>
<#assign title="edit tag '${tag}'"/>
<#assign thisPageHref=gzTagUrl(tag)/>
<#if editValueName?exists == false>
  <#assign editValueName="relatedTags"/>
</#if>
<@pageBegin pageTitle=title cssHref=[gzUrl("/css/gnizr-editusertag.css")]>
<script type="text/javascript" src="${gzUrl("/lib/javascript/edit-tagrel.js")}"></script>
<script type="text/javascript" src="${gzUrl("/data/json/userTagCloud.action?callback=loadUserTags&username="+username)}"></script>
</@pageBegin>
<@headerBlock>
</@headerBlock>
<@pageContent>
<#assign bct = settingsBCT(loggedInUser.username)/>
<#assign bct = bct + [gzBCTPair('edit tag relations', gzUrl("/edit/tag")),
                      gzBCTPair(tag,gzUrl("/settings/tags/edit.action?tag="+tag?url))]/> 
<@infoBlock bct=bct/>
<@mainBlock>
<@pageTitle>Edit Tag Relations</@pageTitle>
<@pageDescription>
Define semantic relationships between <b>'${tag}'</b> and other tags. 
<ol>
<li>Choose a relationship type on the left.</li>
<li>Enter one or more tags on the right.</li>
<li>Tags should be separated with spaces.</li>
<li>Click "Save" when you are done.</li>
</ol>
<p>
<b>Tips</b>: To define relationships between your tags and those of the other users, use Machine Tag "tag". 
</p>
<ul>
<li><code>tag:joe/java</code> -- tag "java" of user "joe"</li>
<li><code>tag:joe/semantic_web</code> -- tag "semantic_web" of user "joe"</li>
</ul>
</@pageDescription>
<div id="editArea">
<div id="controlPanel">
<ul>
<@ww.url id="editRelatedTagsHref" namespace="/settings/tags" action="edit.action" editValueName="relatedTags" tag=tag/>
<li><a href="${editRelatedTagsHref}" rel="${relatedTags}" class="${getEditOptionClass("relatedTags")}"><span class="editTag">${tag}</span> is related to</a></li>
<@ww.url id="editNarrowerTagsHref" namespace="/settings/tags" action="edit.action"  editValueName="narrowerTags" tag=tag/>
<li><a href="${editNarrowerTagsHref}" rel="${narrowerTags}" class="${getEditOptionClass("narrowerTags")}"><span class="editTag">${tag}</span> has narrower</a></li>
<@ww.url id="editBroaderTagsHref" namespace="/settings/tags" action="edit.action" editValueName="broaderTags" tag=tag/>
<li><a href="${editBroaderTagsHref}" rel="${broaderTags}" class="${getEditOptionClass("broaderTags")}"><span class="editTag">${tag}</span> has broader</a></li>
<@ww.url id="editInstanceTagsHref" namespace="/settings/tags" action="edit.action" editValueName="instanceTags" tag=tag/>
<li><a href="${editInstanceTagsHref}" rel="${instanceTags}" class="${getEditOptionClass("instanceTags")}"><span class="editTag">${tag}</span> has group member</a></li>
</ul>
</div>
<div id="editor">
<@ww.form action="save.action" method="post" namespace="/settings/tags">
<@ww.hidden name="tag" value="${tag}"/>
<@ww.hidden name="editValueName" value="${editValueName}"/>
<@ww.textarea name="${editValueName}" cssClass="editor-input" rows="8" id="editValue"/>
<@ww.submit value="save" cssClass="btn"/>
</@ww.form>
<@ww.actionmessage/>
</div>
</div>
</@mainBlock>
<div id="editTools">
<p>
View bookmarks tagged: <a href="${gzUserTagUrl(username,tag)}" class="system-link" target="_blank">${tag}</a>
</p>
<div id="tagCloud">
  <ol id="userTags" class="tag-cloud"></ol>
</div>
</div>
</@pageContent>
<@pageEnd/>

<#function getEditOptionClass name>
  <#if name == editValueName>
    <#return "optionFocused"/>
  <#else>
    <#return "option"/>
  </#if>
</#function>