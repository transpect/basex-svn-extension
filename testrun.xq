import module namespace svn = 'java:io.transpect.basex.extensions.subversion.XSvnApi';
declare variable $auth := map {"username":"","password":""};
(
svn:info('https://subversion.le-tex.de/customers/github-mirror',$auth)
)