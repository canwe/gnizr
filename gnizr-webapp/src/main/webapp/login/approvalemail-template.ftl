A new user account has been created, but requires your approval.

  Username: ${username}
  Email: ${email}
  CreatedOn: ${createdOn?datetime?string}

1) Login as 'gnizr' -- the Super User.

http://gnizer.herokuapp.com/login

2) Aprrove or deny the request.

To approve the request, click the link below:

http://gnizer.herokuapp.com//register/approve.action?username=${username}&token=${token}

To deny the request, click the link below:

http://gnizer.herokuapp.com//register/deny.action?username=${username}&token=${token}

If clicking the link above does not work, copy and paste the URL in a
new browser window instead.

Thank you!


