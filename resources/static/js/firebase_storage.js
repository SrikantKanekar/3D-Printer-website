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
        },
        (error) => {
            console.log(error)
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
        },
        (error) => {
            console.log(error)
        },
        () => {
            uploadTask.snapshot.ref.getDownloadURL().then((url) => {
                downloadURL(url)
            });
        }
    );
}

function deleteFirebaseFolder(id, proceed) {
    let deletedOne = false;

    const deleteTask = storageRef.child(id);
    deleteTask.listAll().then(dir => {
        dir.items.forEach(fileRef => {
            const dirRef = firebase.storage().ref(fileRef.fullPath);
            dirRef.getDownloadURL().then(function(url) {
                const imgRef = firebase.storage().refFromURL(url);
                imgRef.delete().then(function() {
                    if (deletedOne) proceed();
                    deletedOne = true;
                }).catch(function(error) {
                    proceed()
                });
            });
        });
    }).catch(error => {
        proceed()
    });
}