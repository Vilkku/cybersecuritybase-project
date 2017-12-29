The application is based on the base project provided by the course, with some additional features. The application can be found at https://github.com/Vilkku/cybersecuritybase-project.

The first new feature is discussion, allowing visitors to add comments and read comments left by other visitors. There is also a new VIP status for participants. Unfortunately, these features haven’t really been implemented in the most secure manner…

Here are the vulnerability reports:

Issue: SQL Injection
Steps to reproduce:
1. Open Discussion
2. In the Comment field, enter ’); DROP TABLE Comment; --
3. Open Discussion again
4. You now get a Comment table not found error.

Issue: Cross-Site Scripting (XSS)
Steps to reproduce:
1. Open Discussion
2. In the Comment field, enter <script>window.location = “http://www.google.com/”;</script>
3. Open Discussion again
4. The script is executed as soon as the user enters the page, and is redirected.

Issue: Insecure Direct Object References
Steps to reproduce:
1. In the Name field, enter anything
2. In the Address field, enter anything
3. Click Submit
4. Change the number in the URL in the address bar to one smaller than the current one (if larger than 1)
5. You are able to access information about another participant.

Issue: Missing Function Level Access Control
Steps to reproduce:
1. In the Name field, enter anything
2. In the Address field, enter anything
3. Using a tool such as the inspector in your browser, find the hidden input field “vip” and change the value to 1
3. Click Submit
4. You are now enrolled with VIP status

Issue: Security Misconfiguration
Steps to reproduce:
1. Open Discussion
2. In the Comment field, enter “What’s up”?
3. On the error page you are able to see a full stack trace, exposing technical details about the application

Now for some more details about the issues. The SQL Injection, XSS and Security Misconfiguration issues are all related to the new discussion feature. Storing the comments is done using a simple query without any kind of escaping for the comment text, enabling the SQL injection issues, and also exposing the security misconfiguration. For example, as mentioned in the vulnerability report, entering a simple “What’s up?” causes an SQL syntax error that show the query attempting to be executed. From there, it's easy to modify the comment so that a more malicious query is executed, for example dropping the whole comments table. The solution for this would be to use prepared statements.

In addition, this special SQL error page could have been added by a developer trying to figure out why some comments were causing errors, but it also contains names that identify what framework is being used for the application (for example rows containing org.springframework), enabling targeting attacks by finding vulnerabilities for the specific framework. Instead of displaying the stack trace on the error page more detailed logging should be done to a file only the developer can access.

The XSS issue could have been caused by the developer wanting to allow HTML in the comments to let the visitors make them look nicer, but the implementation is done in such a way that everything is allowed, including script tags. Instead of simply inserting unescaped text there should be a whitelist with the tags the developer had in mind, and then handle just them, or use some own tag system (e.g. similar to those you can see on forums) that is then parsed separately.

The access level issues come from a new feature displaying enrollment details after signing up. To identify the enrollment the enrollment ID is added to the URL, but there is no check to verify that it’s actually the recently enrolled participant that is accessing the information. One simple solution for this that would not require any kind of user registration would be to instead of doing a GET request with the ID in the URL the page would instead be displayed straight with the POST request, with all the data still available. The downside of this approach is that you cannot retrieve your details after enrollment.

Finally, the access control issue allows any participant to set their VIP status when enrolling. After enrolling the participant sees references to a VIP status, which might lead to the VIP input in the form. The intention might have been to let admins or certain participants become VIP, but the implementation is such that permission to do so is not checked after submitting the data. Currently there isn’t even any check to show it in the view, it’s simply hidden (the developer might have been annoyed that the controller complained if the input was not included). The input should not be displayed at all unless the person enrolling should have the option to change the value (i.e. even if the participant should be a VIP if the participant isn’t supposed to be able to change it then there should not be a input for it either), and the same checks should be done server side when submitting the data. There should probably be proper user account support added first to ensure that only intended persons get assigned VIP status.
