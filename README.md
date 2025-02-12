NEW GLOBE Exercise

# Bridges-Academics-School-Pupils
Android Engineer Coding Exercise
Objective
Below are a set of requirements from a business owner within Bridge International Academies, relating to a need to be able to administer pupil information from a mobile application.


# **Pupil Management App - README**

## **Overview**
The **Pupil Management App** allows users to **add, delete, view, and manage pupils** while ensuring offline persistence. When offline, new pupils are stored locally and automatically uploaded when the user regains internet access.

---

## **Features**
- **Add a Pupil** with details such as name, country, coordinates, and image.
- **Delete a Pupil** from both the local database and the remote server.
- **View Pupil Details** in a dedicated fragment.
- **Offline Persistence** ensures that pupils are saved locally when offline.
- **Automatic Upload on Reconnection** resumes pending uploads when the user comes back online.

---

## **How to Use the App**

### **1. Adding a Pupil**
To add a new pupil:
1. Open the **Pupil Management App**.
2. Click the **Add Pupil** button.
3. Enter the following details:
    - **Name** (Full name of the pupil).
    - **Country** (Country of origin).
    - **Longitude & Latitude** (Location coordinates).
    - **Image URL** (Link to the pupil's profile picture).
4. Click **Save**.
5. The pupil is stored in the local database and marked as **not uploaded** (`uploaded = false`).
6. If the app detects an active internet connection, the pupil is immediately uploaded to the server.
7. If offline, the pupil remains in the local database and will be uploaded when the internet is restored.

---

### **2. Deleting a Pupil**
To remove a pupil:
1. Navigate to the **Pupil List**.
2. Select the pupil you want to delete.
3. Click the **Delete** button.
4. The pupil is deleted from:
    - The **local database** immediately.
    - The **server** if an internet connection is available.
5. If offline, the pupil will be deleted locally but may still exist on the server until the app synchronizes.

---

### **3. Viewing a Pupil (Pupil Detail Fragment)**
To view detailed information about a pupil:
1. Open the **Pupil List**.
2. Click on a **Pupil’s name**.
3. The **Pupil Detail Fragment** opens, displaying:
    - Name
    - Country
    - Coordinates (Longitude & Latitude)
    - Profile Picture
4. You can navigate back to the list from this screen.

---

### **4. Offline Persistence**
When the app is **offline**, it still allows users to:
- **Add pupils**, which are stored locally but marked as **not uploaded** (`uploaded = false`).
- **Delete pupils**, which removes them from the local database (server deletion happens when online).
- **View existing pupils**, as data is stored persistently in the local database.

---

### **5. Upload Resumes on Reconnection**
If the app detects **restored internet connectivity**, it will:
1. **Check the local database** for any pupils marked as `uploaded = false`.
2. **Upload them to the server** automatically.
3. **Update their status** in the local database (`uploaded = true`).

This ensures that **no pupil is lost** and all records are properly synchronized.

---

## **Technical Implementation**
- **Room Database** is used for offline storage.
- **LiveData & ViewModel** ensure real-time updates.
- **RxJava & Retrofit** handle API communication.
- **Hilt Dependency Injection** is used for efficient data management.

---

## **Conclusion**
This app provides a **seamless experience** for managing pupils, whether online or offline. It ensures **data persistence** and **automated synchronization**, making pupil management efficient and reliable.