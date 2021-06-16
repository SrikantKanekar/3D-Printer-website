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
    antialias: true
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

// GLTF loader
let loader = new THREE.GLTFLoader();
let gltfScene;

function showModel(url, error) {
    updateCanvas();
    loader.load(url, function (gltf) {
        gltfScene = gltf.scene;
        gltf.scene.children[0].traverse(n => {
            if (n.isMesh) {
                n.castShadow = true;
                n.receiveShadow = true;
                if (n.material.map) n.material.map.anisotropy = 1;
            }
        });
        scene.add(gltf.scene);
        renderer.render(scene, camera);
    }, undefined, function (e) {
        error(e);
    });
}

function showModelFake() {
    updateCanvas();
    loader.load('/static/images/scene.gltf', function (gltf) {
        gltfScene = gltf.scene;
        gltf.scene.children[0].traverse(n => {
            if (n.isMesh) {
                n.castShadow = true;
                n.receiveShadow = true;
                if (n.material.map) n.material.map.anisotropy = 1;
            }
        });
        scene.add(gltf.scene);
        renderer.render(scene, camera);
    }, undefined, function (error) {
        console.error(error);
    });
}

function removeModel() {
    scene.remove(gltfScene);
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
