<?xml version="1.0" encoding="UTF-8"?>
<#include "/lib/web/macro-lib.ftl"/>
<OpenSearchDescription 
  xmlns="http://a9.com/-/spec/opensearch/1.1/"
  xmlns:gn="http://gnizr.com/ont/opensearch/2007/11/">
  <ShortName>Bookmarked by you</ShortName>
  <Description>Search my saved bookmarks in ${getSiteName()}</Description>
  <Tags>bookmarks gnizr</Tags>
  <gn:DefaultEnabled>true</gn:DefaultEnabled>
  <Url type="application/vnd.gn-opensearch+json" 
       template="${gzUrl("/data/json/user/searchBookmark.action?queryString={searchTerms}&amp;page={startPage}")}"/>
  <gn:LoginRequired>true</gn:LoginRequired>
</OpenSearchDescription>