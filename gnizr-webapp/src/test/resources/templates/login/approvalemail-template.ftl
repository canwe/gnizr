ApprovalEmail
Username: ${username}
Email(${email})
CreatedOn: ${createdOn?datetime?string}
Username(${username})
URL(${gnizrConfiguration.webApplicationUrl}/register/verifyEmail.action?username=${username}&token=${token})