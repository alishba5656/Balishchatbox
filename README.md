Project Title: Balish Chat Box
Course: Mobile Application Development
Program: BSCS / BSSE – Semester 5
________________________________________
1) List of Ten Features / Functional Requirements
(Balish Chat Box – Chat Application)
The Balish Chat Box is an Android-based chat application that allows users to communicate through text messages while maintaining application state across various Android OS events. The following are the core functional requirements of the project:
1.	User Registration
Allows new users to create an account using a username and password stored locally.
2.	User Login & Authentication
Enables registered users to securely log in to the application using local data storage.
3.	One-to-One Chat Messaging
Allows users to send and receive text messages in a chat interface.
4.	Message History Storage
Saves chat messages locally so that previous conversations can be viewed later.
5.	User Session Management
Maintains login state even when the app is minimized or reopened.
6.	State Preservation on Screen Rotation
Preserves typed messages and chat state during screen rotation or configuration changes.
7.	Background and Foreground Handling
Ensures chat data remains intact when the app moves between background and foreground.
8.	Low-Memory Process Recovery
Restores user and chat state if the app is killed due to low memory.
9.	Simple and User-Friendly Interface
Provides a clean and intuitive UI for smooth user interaction. 
10. Logout Functionality
Allows users to safely log out and clear session data.







2) Feasibility Study
(Mapping Features to Resources / APIs / Libraries)
Feature No.	Feature Name	Resources / APIs / Libraries Used	Feasibility Explanation
1	User Registration	SQLite Database	SQLite allows efficient local storage of user credentials without internet dependency.
2	User Login	SQLite + SharedPreferences	SQLite validates credentials; SharedPreferences stores login state.
3	One-to-One Chat	RecyclerView	RecyclerView efficiently displays chat messages in list form.
4	Message History	SQLite Database	Messages are stored locally for persistence and retrieval.
5	Session Management	SharedPreferences	Stores login status to maintain session across app restarts.
6	Rotation Handling	onSaveInstanceState()	Saves UI state during configuration changes like rotation.
7	Background/Foreground Handling	Lifecycle Methods (onPause/onResume)	Ensures smooth transition without data loss.
8	Low-Memory Recovery	ViewModel / Bundle	Restores critical data when app is recreated.
9	User Interface	XML Layouts + Material Components	XML ensures responsive UI with minimal overhead.
10	Logout	SharedPreferences	Clears saved session data safely and efficiently.




3) Technical Developer Guide (Short)
Feature 1: User Authentication
Firebase Authentication is used to register and log in users using email and password. The login and signup screens are created using XML. FirebaseAuth methods handle user creation, login, and session management. Logged-in users are redirected directly to the chat screen.
Feature 2: Real-Time Chat Messaging
Firebase Realtime Database / Firestore is used to store and retrieve chat messages. Messages are sent to Firebase on button click and displayed using RecyclerView. Real-time listeners automatically update the chat interface when new messages are added.
Feature 3: State Preservation
Android lifecycle methods and Firebase session handling are used to preserve app state during screen rotation, background execution, and app restarts. User login state and chat history are restored automatically to ensure smooth user experience.

<img width="360" height="759" alt="image" src="https://github.com/user-attachments/assets/18c67166-1b7d-47f3-b718-845ee7123903" />
<img width="400" height="815" alt="image" src="https://github.com/user-attachments/assets/2b31f1a4-c650-426b-bdf5-6d053cd99403" />
<img width="356" height="754" alt="image" src="https://github.com/user-attachments/assets/e124da99-d173-4474-a774-b0ca3b81d8c7" />
<img width="359" height="760" alt="image" src="https://github.com/user-attachments/assets/7a2ff247-fb2e-4eba-ab63-c89e5680171d" />
<img width="358" height="764" alt="image" src="https://github.com/user-attachments/assets/5bb06a59-0245-426d-b451-01f29d210b42" />
<img width="365" height="763" alt="image" src="https://github.com/user-attachments/assets/ad3ce3c4-909f-4af0-bc01-eaadb2191dc1" />
<img width="364" height="771" alt="image" src="https://github.com/user-attachments/assets/4732db18-4644-44b7-9602-d3cfc2e67145" />
<img width="390" height="860" alt="image" src="https://github.com/user-attachments/assets/86ff46bb-72bc-4ecf-b4fe-ebb70f53ec7f" />







