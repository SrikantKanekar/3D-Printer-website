const firebaseConfig = {
    apiKey: "AIzaSyBTDlOidvCChxu7jqKPUOop-blmW-NJXJU",
    authDomain: "design-server-312705.firebaseapp.com",
    projectId: "design-server-312705",
    storageBucket: "design-server-312705.appspot.com",
    messagingSenderId: "1097772976324",
    appId: "1:1097772976324:web:4e350cb448bea6197ce191"
};
firebase.initializeApp(firebaseConfig);

const storageRef = firebase.storage().ref();

function uploadFirebaseFile(file, filename, id, progress, downloadURL) {
    const uploadTask = storageRef.child(id + '/' + filename).put(file);

    uploadTask.on(firebase.storage.TaskEvent.STATE_CHANGED,
        (snapshot) => {

            progress((snapshot.bytesTransferred / snapshot.totalBytes) * 100);

            switch (snapshot.state) {
                case firebase.storage.TaskState.PAUSED:
                    console.log('Upload is paused');
                    break;
                case firebase.storage.TaskState.RUNNING:
                    console.log('Upload is running');
                    break;
            }
        },
        (error) => {
            switch (error.code) {
                case 'storage/unauthorized':
                    console.log('User doesn\'t have permission to access the object');
                    break;
                case 'storage/canceled':
                    console.log('User canceled the upload');
                    break;
                case 'storage/unknown':
                    console.log('Unknown error occurred, inspect error.serverResponse');
                    break;
            }
        },
        () => {
            uploadTask.snapshot.ref.getDownloadURL().then((url) => {
                downloadURL(url)
            });
        }
    );
}

function uploadFirebaseImage(image, id, progress, downloadURL) {
    const uploadTask = storageRef.child(id + '/image').putString(image, 'data_url');

    uploadTask.on(firebase.storage.TaskEvent.STATE_CHANGED,
        (snapshot) => {

            progress((snapshot.bytesTransferred / snapshot.totalBytes) * 100);

            switch (snapshot.state) {
                case firebase.storage.TaskState.PAUSED:
                    console.log('Upload is paused');
                    break;
                case firebase.storage.TaskState.RUNNING:
                    console.log('Upload is running');
                    break;
            }
        },
        (error) => {
            switch (error.code) {
                case 'storage/unauthorized':
                    console.log('User doesn\'t have permission to access the object');
                    break;
                case 'storage/canceled':
                    console.log('User canceled the upload');
                    break;
                case 'storage/unknown':
                    console.log('Unknown error occurred, inspect error.serverResponse');
                    break;
            }
        },
        () => {
            uploadTask.snapshot.ref.getDownloadURL().then((url) => {
                downloadURL(url)
            });
        }
    );
}

function deleteFirebaseFolder(id) {
    // firebase dont allow to delete folder
    // const deleteTask = storageRef.child(id);
    // deleteTask.delete().then(() => {
    //     console.log("deleted")
    // }).catch((error) => {
    //     console.log(error)
    // });
}