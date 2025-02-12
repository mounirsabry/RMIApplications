# RMIApplications
Employee Management System and Broadcast Chat applications using Java RMI for ITI
Both has Javafx interface.

A mini project that spiked my interest while taking IO and Network Programming course from ITI with Prof Amr El-Shafey.

I wrote two mini programs, once relies on one-way RMI.
the second use RMI with callback.

The first application is a small employee management system that allows the user/admin
to:
1. View the employees.
2. Add a new employee.
3. Update the info of an employee.
4. Delete an employee.

The programs connects to a server (separate program) using rmi to do the operations it needs.
--------------
The second program is a broadcast chatting application.

It is a mini project that allows users to
1. Log in into the system (using a username), no registration required.
2. View all the broadcasted messages.
3. Receive the new messages from other users using Callback.
4. Send/broadcast a message, the message will sent to all the users in the system using Callback.
5. Track the currently online users.
6. Track the user status joining and leaving the chat.

Key features:
1. If the user left the chat by will or force, his/her messages are not deleted, they are stored, so if they decided to come
back and continue chatting.

2. The user can log in, log out, as much as he wants without closing the program.
   
3. Auto cleanup, if the admin decided to shutdown the server, the messages will be automatically deleted, no need to remove the messages manually.
--------------
Used Technologies.
------------------
1. Java to write the frontend and backend parts of the applications.
2. RMI using java to connect the frontend and backend together.
3. Callback in RMI to notify the user about other users arrival and messages.
4. To store the data of the employees.
