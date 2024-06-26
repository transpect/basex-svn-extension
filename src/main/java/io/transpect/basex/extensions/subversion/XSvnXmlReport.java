package io.transpect.basex.extensions.subversion;

import java.io.File;

import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNLock;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.io.SVNRepository;

import org.basex.query.value.node.FNode;
import org.basex.query.value.node.FBuilder;

/**
 * Returns reports or errors as XML (currently only
 * string) that needs to be parsed with parse-xml()
 */
public class XSvnXmlReport {

  final static String nsprefix = "c";
  final static String nsuri = "http://www.w3.org/ns/xproc-step";
    
  /**
   * Render a HashMap as XML c:param-set
   */
  public static FNode createXmlResult(HashMap<String, String> results) {
    FBuilder xmlResult = XSvnHelper.build( "param-set", nsuri, nsprefix);
    for(String key:results.keySet()) {
      FBuilder paramElement = XSvnHelper.build("param", nsuri, nsprefix);
      paramElement = XSvnHelper.attach(paramElement,"name",key);
      paramElement = XSvnHelper.attach(paramElement,"value",results.get(key));
      xmlResult.add(paramElement);
    }
    return xmlResult.finish();
  }
  public static FNode createXmlResult(String baseURI, String type, String[] results) {
    FBuilder xmlResult = XSvnHelper.build("param-set", nsuri, nsprefix);
    xmlResult = XSvnHelper.attach(xmlResult,"xml:base",baseURI);
    for(int i = 0; i < results.length; i++){
      FBuilder paramElement = XSvnHelper.build("param", nsuri, nsprefix);
      paramElement = XSvnHelper.attach(paramElement,"name",type);
      paramElement = XSvnHelper.attach(paramElement,"value",results[i]);
      xmlResult.add(paramElement);
    }
    return xmlResult.finish();
  }
  public static FNode createXmlDirTree(File path, Boolean recursive) throws SVNException {
    FBuilder element = XSvnHelper.build("files", nsuri, nsprefix);
    element = XSvnHelper.attach(element, "xml:base", path.toURI().toString());
    element = listEntries(path, recursive, element); 
    return element.finish();
  }

  public static FBuilder listEntries(File path, Boolean recursive, FBuilder dirElement) throws SVNException {
    File[] dirList = path.listFiles();
    if( path.isDirectory() && dirList != null ) {
      for (File child : dirList ) {
        String elementName = child.isDirectory() ? "directory" : "file";
        FBuilder element = XSvnHelper.build(elementName, nsuri, nsprefix);
        element = XSvnHelper.attach(element,"name", child.getName());
        if(!child.isDirectory()) {
          element = XSvnHelper.attach(element, "size", String.valueOf(child.length() / 1024));
        }
        if(child.isDirectory() && recursive) {
          dirElement = listEntries(child, recursive, element);
        }
        dirElement.add(element);
      }
    }
    return dirElement;
  }
  public static FNode createXmlDirTree(String url, SVNRepository repository, Boolean recursive) throws SVNException {
    FBuilder xmlResult = XSvnHelper.build("files", nsuri, nsprefix);
    xmlResult = XSvnHelper.attach(xmlResult,"xml:base", url);
    xmlResult = listEntries(repository, "", recursive, xmlResult);
    return xmlResult.finish();
  }
  public static FBuilder listEntries(SVNRepository repository, String path, Boolean recursive, FBuilder dirElement) throws SVNException {
    Collection entries = repository.getDir(path, -1 , null , (Collection) null);
    Iterator iterator = entries.iterator();
    String repositoryRootURL = repository.getRepositoryRoot(true).toString();
    while (iterator.hasNext()) {
      SVNDirEntry entry = (SVNDirEntry) iterator.next( );
      String elementName = entry.getKind() == SVNNodeKind.DIR ? "directory" : "file";
      String entryURL = entry.getURL().toString();
      String entryRelPath = entryURL.replace(repositoryRootURL, "");
      FBuilder element = XSvnHelper.build(elementName, nsuri, nsprefix);
      element = XSvnHelper.attach(element, "name", entry.getName());
      element = XSvnHelper.attach(element, "author", entry.getAuthor());
      element = XSvnHelper.attach(element, "date", entry.getDate().toString());
      element = XSvnHelper.attach(element, "revision", String.valueOf(entry.getRevision()));
      if ( entry.getKind() == SVNNodeKind.FILE ) {
          SVNLock lock = repository.getLock( entryRelPath );
          element = XSvnHelper.attach(element, "size", String.valueOf( entry.getSize() ));
          if( lock != null ) {
            element = XSvnHelper.attach(element, "lock-id", lock.getID());
            element = XSvnHelper.attach(element, "lock-path", lock.getPath());
            element = XSvnHelper.attach(element, "lock-owner", lock.getOwner());
            element = XSvnHelper.attach(element, "lock-created", lock.getCreationDate().toString());
            if( lock.getExpirationDate() != null ) {
              element = XSvnHelper.attach(element, "lock-expires", lock.getExpirationDate().toString());
            }
            if( lock.getComment() != null ) {
              element = XSvnHelper.attach(element, "lock-comment", lock.getComment());
            }
          }
      }
      if (entry.getKind() == SVNNodeKind.DIR && recursive == true) {
        element = listEntries(repository, (path.equals( "" )) ? entry.getName( ) : path + "/" + entry.getName( ), recursive, element);
      }
      dirElement.add(element);
    }
    return dirElement;
  }
  /**
   * Render errors as XML c:errors
   */
  public static FNode createXmlError(String message) {
    FBuilder xmlResult = XSvnHelper.build("errors", nsuri, nsprefix);

    xmlResult = XSvnHelper.attach(xmlResult, "code", "svn-error");
    FBuilder errorElement = XSvnHelper.build("error", nsuri, nsprefix);
    errorElement = XSvnHelper.attach(errorElement,"code", "error");
    errorElement.add(message);
    xmlResult.add(errorElement);
    return xmlResult.finish();
  }
}
