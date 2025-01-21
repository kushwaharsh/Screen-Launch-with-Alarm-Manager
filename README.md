## 🚀 How to Launch a Screen When the App is in Background, Foreground, or Killed 📱

### 📖 Overview
This project demonstrates how to launch a screen in an Android app across different app states—foreground, background, and even when the app is killed. By utilizing WakeLock, BroadcastReceiver, and Services, we ensure that the app remains responsive and behaves as expected, even when not in the foreground.

### 🔑 Key Features:
* Launch screens even when the app is in the background or killed.
* Use WakeLock to keep the CPU active in a low-power state.
* Trigger activities and notifications with BroadcastReceiver and AlarmManager.
* Handle app behavior effectively across different Android versions and app states.

### 🎬 Key Components

* **AlarmReceiver** : A BroadcastReceiver that listens for system alarms and triggers actions, like launching an activity.
* **AlarmService** : An IntentService that runs in the foreground to handle alarm-related tasks, ensuring the app stays active.
* **HomeScreen Activity** : The screen that gets triggered when the app is in a specific state (foreground, background, or killed).


### 📝 Code Walkthrough
* **MainActivity** : The entry point of the app. It initializes the user interface and starts activities based on user actions, such as triggering alarms.
* **AlarmReceiver** : Listens for broadcast intents and wakes up the device to trigger an activity.
* **AlarmService** : Runs a foreground service that keeps the app responsive and launches a notification.
* **HomeScreen Activity** : Displays the content based on the system's trigger (alarm or notification).
* **Manifest File** : Configures permissions, declares activities, and sets up necessary services. It ensures the app can handle alarms, notifications, and foreground services, as well as manage behavior when the app is in the background or killed.


### 🤝 Contributing
Feel free to fork this repository, make changes, and submit a pull request. If you have any suggestions, bug fixes, or improvements, I’d love to hear from you!

### 📇 Connect with Me
Feel free to connect with me on LinkedIn:

🔗 [LinkedIn](https://www.linkedin.com/in/kushwaharsh/)


#### ✨ Enjoy the project and happy coding! ✨
