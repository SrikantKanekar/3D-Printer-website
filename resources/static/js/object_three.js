// Canvas
const canvasContainer = document.querySelector(".canvas_container");
const canvas = document.querySelector('#canvas');

const sizes = {
    width: canvasContainer.clientWidth,
    height: 0.8 * canvasContainer.clientWidth
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
loader.load('/static/images/skull_downloadable/scene.gltf', function (gltf) {
    const model = gltf.scene.children[0];
    model.traverse(n => {
        if (n.isMesh) {
            n.castShadow = true;
            n.receiveShadow = true;
            if (n.material.map) n.material.map.anisotropy = 1;
        }
    });
    scene.add(gltf.scene);
    animate();
}, undefined, function (error) {
    console.error(error);
});

// OrbitControls
let controls = new THREE.OrbitControls(camera, canvas);
controls.enableDamping = true;
controls.update();

function animate() {
    requestAnimationFrame(animate);
    controls.update();
    spotLight.position.set(
        camera.position.x + 10,
        camera.position.y + 10,
        camera.position.z + 10,
    );
    renderer.render(scene, camera);
}

// browser resize
window.addEventListener('resize', () => {
    // Update sizes
    sizes.width = canvasContainer.clientWidth;
    sizes.height = 0.8 * canvasContainer.clientWidth;

    // Update camera
    camera.aspect = sizes.width / sizes.height;
    camera.updateProjectionMatrix();

    // Update renderer
    renderer.setSize(sizes.width, sizes.height);
    renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));
});

// Gui Controller
const gui = new dat.GUI({autoPlace: false});
gui.close();
gui.domElement.id = "gui";
canvasContainer.appendChild(gui.domElement);

const cameraFolder = gui.addFolder("Camera")
cameraFolder.add(camera.position, "x", 0, 10, 0.01).listen()
cameraFolder.add(camera.position, "y", 0, 10, 0.01).listen()
cameraFolder.add(camera.position, "z", 0, 10, 0.01).listen()
