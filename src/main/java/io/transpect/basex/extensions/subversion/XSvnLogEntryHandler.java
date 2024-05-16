package io.transpect.basex.extensions.subversion;

import java.util.Map;
import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;

import org.basex.query.value.node.FElem;
import org.basex.query.value.node.FNode;
import org.basex.query.value.node.FBuilder;
import org.basex.query.value.item.QNm;
/**
 * Performs svn list. The class connects to a Subversion repository
 * and provides the results as XML directory tree.
 *
 * @see XSvnConnect
 */
public class XSvnLogEntryHandler implements ISVNLogEntryHandler {
  
  final static String nsprefix = "c";
  final static String nsuri = "http://www.w3.org/ns/xproc-step";
  
  public String Result;
  private FBuilder XmlResult;
  
  public XSvnLogEntryHandler()
  {
    this.XmlResult = FElem.build(new QNm("log"));
  }

  public FNode getResult()
  {
    return this.XmlResult.finish();
  }
	
	public static FBuilder createXmlFromPaths(Map<String, SVNLogEntryPath> results) {
    FBuilder xmlResult = XSvnHelper.build("changedPaths",nsuri,nsprefix);
		xmlResult.add(new QNm("changes"), "" + results.size());

    for(String key:results.keySet()) {
      FBuilder pathElement = XSvnHelper.build("changedPath", nsuri, nsprefix);
			SVNLogEntryPath path = results.get(key);
      pathElement.add(new QNm("name"),key.getBytes());
      pathElement.add(new QNm("path"),path.getPath().getBytes());
      pathElement.add(new QNm("type"),path.getType());
      xmlResult.add(pathElement);
    }
    return xmlResult;
  }
  
  public void handleLogEntry (SVNLogEntry entry){
    
    Result = Result + '\n' + entry.toString();
    
    String elementName = "logEntry";
    FBuilder element = XSvnHelper.build(elementName,nsuri,nsprefix);
    element.add(new QNm("author"),entry.getAuthor().getBytes());
    element.add(new QNm("date"),String.valueOf(entry.getDate()).getBytes());
    element.add(new QNm("message"),entry.getMessage().getBytes());
    element.add(new QNm("revision"),Long.toString(entry.getRevision()).getBytes());

		FBuilder changedPaths = createXmlFromPaths(entry.getChangedPaths());
		element.add(changedPaths);
		
    XmlResult.add(element);
  }
}
