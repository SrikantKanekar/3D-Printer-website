// Canvas
const canvasContainer = document.querySelector(".canvas_container");
const canvas = document.querySelector('#canvas');

const sizes = {
    width: canvasContainer.clientWidth,
    height: canvasContainer.clientHeight
}

// scene
const scene = new THREE.Scene();
scene.background = new THREE.Color(0xdddddd);

// axes
// const axesHelper = new THREE.AxesHelper(5);
// scene.add(axesHelper);

// camera
camera = new THREE.PerspectiveCamera(60, sizes.width / sizes.height, 0.1, 1000);
camera.position.set(2, 2, 2);

// OrbitControls
let controls = new THREE.OrbitControls(camera, canvas);
controls.update();
controls.addEventListener('change', () => renderer.render(scene, camera));

// renderer
const renderer = new THREE.WebGLRenderer({
    canvas: canvas,
    antialias: true,
    preserveDrawingBuffer: true
});
renderer.toneMapping = THREE.ReinhardToneMapping;
renderer.toneMappingExposure = 2.3;
renderer.setSize(sizes.width, sizes.height);
renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));
renderer.shadowMap.enabled = true;

// lightning
const hemiLight = new THREE.HemisphereLight(0xffeeb1, 0x080820, 4);
scene.add(hemiLight);

spotLight = new THREE.SpotLight(0xffa95c, 4);
spotLight.position.set(-50, 50, 50);
spotLight.castShadow = true;
spotLight.shadow.bias = -0.0001;
spotLight.shadow.mapSize.width = 1024 * 4;
spotLight.shadow.mapSize.height = 1024 * 4;
scene.add(spotLight);

const manager = new THREE.LoadingManager();
manager.onLoad = function () {
    $('#canvasLoader').delay(100).fadeOut('slow', function () {
        $(this).remove();
    });
}
manager.onError = function (url) {
    const errorContainer = document.querySelector('.canvas_error_container');
    errorContainer.style.display = "flex";
    errorContainer.firstElementChild.textContent = "Error loading " + url;
}

let currentModel;

function showModel(url, extension, error, sizeError) {
    updateCanvas();

    if (extension === "glb") {
        new THREE.GLTFLoader(manager).load(url, function (gltf) {
            currentModel = gltf.scene;
            gltf.scene.children[0].traverse(n => {
                if (n.isMesh) {
                    n.castShadow = true;
                    n.receiveShadow = true;
                    if (n.material.map) n.material.map.anisotropy = 1;
                }
            });
            scene.add(gltf.scene);
            renderer.render(scene, camera);
            const verified = verifyModel();
            sizeError(verified);
        }, undefined, function (e) {
            error(e);
            const errorContainer = document.querySelector('.canvas_error_container');
            errorContainer.style.display = "flex";
            errorContainer.firstElementChild.textContent = e.message;
        });
    } else if (extension === "obj") {
        new THREE.OBJLoader(manager).load(url, function (object) {
                currentModel = object;
                scene.add(object);
            }, undefined, function (error) {
                console.log(error);
            }
        );
    } else if (extension === "STL") {
        new THREE.STLLoader(manager).load(url, function (geometry) {
                const material = new THREE.MeshPhysicalMaterial({
                    color: 0xb2ffc8,
                    metalness: .25,
                    roughness: 0.1,
                    transparent: true,
                    transmission: 1.0,
                    side: THREE.DoubleSide,
                    clearcoat: 1.0,
                    clearcoatRoughness: .25
                });
                const mesh = new THREE.Mesh(geometry, material)
                currentModel = mesh;
                scene.add(mesh)
            }, undefined, (error) => {
                console.log(error);
            }
        );
    }
}

function removeModel() {
    scene.remove(currentModel);
    renderer.render(scene, camera);
}

// browser resize
window.addEventListener('resize', () => {
    updateCanvas();
});

function updateCanvas() {
    // Update sizes
    sizes.width = canvasContainer.clientWidth;
    sizes.height = canvasContainer.clientHeight;

    // Update camera
    camera.aspect = sizes.width / sizes.height;
    camera.updateProjectionMatrix();

    // Update renderer
    renderer.setSize(sizes.width, sizes.height);
    renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));
    renderer.render(scene, camera)
}

// render
function render() {
    renderer.render(scene, camera);
    controls.update();
}

// Gui Controller
const gui = new dat.GUI({autoPlace: false});
gui.close();
gui.domElement.id = "gui";
canvasContainer.appendChild(gui.domElement);

const cameraFolder = gui.addFolder("Camera")
cameraFolder.add(camera.position, "x", 0, 10, 0.01).onChange(render).listen()
cameraFolder.add(camera.position, "y", 0, 10, 0.01).onChange(render).listen()
cameraFolder.add(camera.position, "z", 0, 10, 0.01).onChange(render).listen()


function verifyModel() {
    return true;
}

// function takeSnapshot(q) {
//     const temporaryCanvas = document.createElement('canvas');
//     let context = temporaryCanvas.getContext('2d');
//
//     let cWidth = canvas.width;
//     let cHeight = canvas.height;
//     let smaller;
//     let larger;
//
//     if (cWidth < cHeight) {
//         smaller = cWidth;
//         larger = cHeight;
//     } else {
//         smaller = cHeight;
//         larger = cWidth;
//     }
//
//     let newSize = smaller / q;
//     temporaryCanvas.width = newSize;
//     temporaryCanvas.height = newSize;
//
//     let x = larger / 2 - smaller / 2;
//
//     if (cWidth < cHeight) {
//         context.drawImage(canvas, 0, x, smaller, smaller, 0, 0, newSize, newSize);
//     } else {
//         context.drawImage(canvas, x, 0, smaller, smaller, 0, 0, newSize, newSize);
//     }
//     return temporaryCanvas.toDataURL("image/png");
// }

function takeSnapshot() {
    return canvas.toDataURL("image/png");
}