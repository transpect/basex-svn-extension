package io.transpect.basex.extensions.subversion;

import org.basex.query.value.node.FNode;
import org.basex.query.value.map.XQMap;
import org.basex.query.value.item.*;

/**
 * 
 * XSvn BaseX extension
 *
 */
public class XSvnApi  {
	
	private XQMap createauth(String username, String password){
		Str usernamevalue = Str.get(username);
		Str usernamekey = Str.get("username");
		Str passwordvalue = Str.get(password);
		Str passwordkey = Str.get("password");
		
		try {
			XQMap result = XQMap.singleton(usernamekey, usernamevalue);
			result.put(passwordkey,passwordvalue);
			return result;
		}
		catch (Exception e){
			System.out.println(e.toString());
			return null;
		}
	}
	
  public FNode info (String url, String username, String password) {
    XSvnInfo info = new XSvnInfo();
		XQMap auth = createauth(username, password);
    return info.XSvnInfo(url, auth);
  }
  public FNode info (String url, XQMap auth) {
    XSvnInfo info = new XSvnInfo();
    return info.XSvnInfo(url, auth);
  }

  public FNode status (String url, XQMap auth) {
    XSvnStatus status = new XSvnStatus();
    return status.XSvnStatus(url, auth);
  }
	
  public FNode list (String url, String username, String password, Boolean recursive) {
    XSvnList list = new XSvnList();
		XQMap auth = createauth(username, password);
    return list.XSvnList(url, auth, recursive);
  }
	public FNode list (String url, XQMap auth, Boolean recursive) {
    XSvnList list = new XSvnList();
    return list.XSvnList(url, auth, recursive);
  }

	public FNode look (String url, String path, XQMap auth, Boolean recursive) {
    XSvnList list = new XSvnList();
		String localpath = path != null?path:"/";
    return list.XSvnLook(url, localpath, auth, recursive);
  }
	
  public FNode checkout (String url, String username, String password, String path, String revision, String depth) {
    XSvnCheckout checkout = new XSvnCheckout();
		XQMap auth = createauth(username, password);
    return checkout.XSvnCheckout(url, auth, path, revision, depth);
  }
	public FNode checkout (String url, XQMap auth, String path, String revision, String depth) {
    XSvnCheckout checkout = new XSvnCheckout();
		return checkout.XSvnCheckout(url, auth, path, revision, depth);
  }
	
  public FNode copy (String url, String username, String password, String path, String target, String commitMessage) {
    XSvnCopy copy = new XSvnCopy();
		XQMap auth = createauth(username, password);
    return copy.XSvnCopy(url, auth, path, target, false, commitMessage);
  }
	public FNode copy (String url, XQMap auth, String path, String target, String commitMessage) {
    XSvnCopy copy = new XSvnCopy();
		return copy.XSvnCopy(url, auth, path, target, false, commitMessage);
  }
	
  public FNode move (String url, String username, String password, String path, String target, String commitMessage) {
    XSvnCopy move = new XSvnCopy();
		XQMap auth = createauth(username, password);
    return move.XSvnCopy(url, auth, path, target, true, commitMessage);
  }
	public FNode move (String url, XQMap auth, String path, String target, String commitMessage) {
    XSvnCopy move = new XSvnCopy();
		return move.XSvnCopy(url, auth, path, target, true, commitMessage);
  }
	
  public FNode mkdir (String url, String username, String password, String dir, Boolean parents, String commitMessage) {
    XSvnMkDir mkdir = new XSvnMkDir();
		XQMap auth = createauth(username, password);
    return mkdir.XSvnMkDir(url, auth, dir, parents, commitMessage);
  }
	public FNode mkdir (String url, XQMap auth, String dir, Boolean parents, String commitMessage) {
    XSvnMkDir mkdir = new XSvnMkDir();
		return mkdir.XSvnMkDir(url, auth, dir, parents, commitMessage);
  }
  public FNode mkdir (String url, XQMap auth, String dir, Boolean parents) {
    XSvnMkDir mkdir = new XSvnMkDir();
		return mkdir.XSvnMkDir(url, auth, dir, parents, "");
  }
	
  public FNode add (String url, String username, String password, String path, Boolean parents) {
    XSvnAdd add = new XSvnAdd();
		XQMap auth = createauth(username, password);
    return add.XSvnAdd(url, auth, path, parents);
  }
  public FNode add (String url, XQMap auth, String path, Boolean parents) {
    XSvnAdd add = new XSvnAdd();
		return add.XSvnAdd(url, auth, path, parents);
  }
	
  public FNode delete (String url, String username, String password, String path, Boolean force, String commitMessage) {
    XSvnDelete delete = new XSvnDelete();
		XQMap auth = createauth(username, password);
    return delete.XSvnDelete(url, auth, path, force, commitMessage);
  }
	public FNode delete (String url, XQMap auth, String path, Boolean force, String commitMessage) {
    XSvnDelete delete = new XSvnDelete();
		return delete.XSvnDelete(url, auth, path, force, commitMessage);
  }
  public FNode delete (String url, XQMap auth, String path, Boolean force) {
    XSvnDelete delete = new XSvnDelete();
		return delete.XSvnDelete(url, auth, path, force, "");
  }
	
  public FNode update (String username, String password, String path, String revision) {
    XSvnUpdate update = new XSvnUpdate();
		XQMap auth = createauth(username, password);
    return update.XSvnUpdate(auth, path, revision);
  }
	public FNode update (XQMap auth, String path, String revision) {
    XSvnUpdate update = new XSvnUpdate();
		return update.XSvnUpdate(auth, path, revision);
  }
	public FNode update (XQMap auth, String path) {
    XSvnUpdate update = new XSvnUpdate();
		return update.XSvnUpdate(auth, path, "");
  }
	
  public FNode commit (String username, String password, String path, String commitMessage) {
    XSvnCommit commit = new XSvnCommit();
		XQMap auth = createauth(username, password);
    return commit.XSvnCommit(auth, path, commitMessage);
  }
	public FNode commit (XQMap auth, String path, String commitMessage) {
    XSvnCommit commit = new XSvnCommit();
		return commit.XSvnCommit(auth, path, commitMessage);
  }
	
  public FNode propget (String url, String username, String password, String property, String revision) {
    XSvnPropGet propget = new XSvnPropGet();
		XQMap auth = createauth(username, password);
    return propget.XSvnPropGet(url, auth, property, revision);
  }
	public FNode propget (String url, XQMap auth, String property, String revision) {
    XSvnPropGet propget = new XSvnPropGet();
		return propget.XSvnPropGet(url, auth, property, revision);
  }
	
  public FNode propset (String url, String username, String password, String propName, String propValue, String revision) {
    XSvnPropSet propset = new XSvnPropSet();
		XQMap auth = createauth(username, password);
    return propset.XSvnPropSet(url, auth, propName, propValue, revision);
  }
	public FNode propset (String url, XQMap auth, String propName, String propValue, String revision) {
    XSvnPropSet propset = new XSvnPropSet();
		return propset.XSvnPropSet(url, auth, propName, propValue, revision);
  }
	
	  public FNode log (String url, String username, String password, int revisionStart, int revisionEnd, int limit) {
    XSvnLog log = new XSvnLog();
		XQMap auth = createauth(username, password);
    return log.XSvnLog(url, auth, revisionStart, revisionEnd, limit);
  }
	public FNode log (String url, XQMap auth, int revisionStart, int revisionEnd, int limit) {
    XSvnLog log = new XSvnLog();
		return log.XSvnLog(url, auth, revisionStart, revisionEnd, limit);
  }
	public FNode log (String url, XQMap auth) {
    XSvnLog log = new XSvnLog();
		return log.XSvnLog(url, auth, -1, -1, -1);
  }
	
  public FNode lock (String url, String username, String password, String paths, String message) {
    XSvnLock lock = new XSvnLock();
		XQMap auth = createauth(username, password);
    return lock.XSvnLock(url, auth, paths, false, false, message);
  }
	public FNode lock (String url, XQMap auth, String paths, String message) {
    XSvnLock lock = new XSvnLock();
		return lock.XSvnLock(url, auth, paths, false, false, message);
  }
	
  public FNode unlock (String url, String username, String password, String paths, String message) {
    XSvnLock unlock = new XSvnLock();
		XQMap auth = createauth(username, password);
    return unlock.XSvnLock(url, auth, paths, true, false, message);
  }
	public FNode unlock (String url, XQMap auth, String paths, String message) {
    XSvnLock unlock = new XSvnLock();
		return unlock.XSvnLock(url, auth, paths, true, false, message);
  }
}
