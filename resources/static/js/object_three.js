// Canvas
const canvasContainer = document.querySelector(".canvas_container");
const canvas = document.querySelector('#canvas');
let currentModel;

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
camera = new THREE.PerspectiveCamera(75, sizes.width / sizes.height, 0.1, 1000);

// OrbitControls
let controls = new THREE.OrbitControls(camera, canvas);
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

initLighting();

function initLighting() {
    const hemiLight = new THREE.HemisphereLight(0xffeeb1, 0x080820, 4);
    scene.add(hemiLight);

    const spotLight = new THREE.SpotLight(0xffa95c, 4);
    spotLight.position.set(-50, 50, 50);
    spotLight.castShadow = true;
    spotLight.shadow.bias = -0.0001;
    spotLight.shadow.mapSize.width = 1024 * 4;
    spotLight.shadow.mapSize.height = 1024 * 4;
    scene.add(spotLight);
}

// loading manager
const manager = new THREE.LoadingManager();
manager.onLoad = function () {
    $('#canvasLoader').delay(100).fadeOut('slow', function () {
        $(this).remove();
    });
}
manager.onError = function (url) {
    showCanvasError("Error loading " + url)
}

// material for obj and stl files
const material = new THREE.MeshPhysicalMaterial({
    color: 0xffffff,
    metalness: .25,
    roughness: 0.1,
    transparent: true,
    transmission: 1.0,
    side: THREE.DoubleSide,
    clearcoat: 1.0,
    clearcoatRoughness: .25
});

function showModel(url, extension, error, sizeError) {
    updateCanvasDimension();
    if (extension === "glb") {
        new THREE.GLTFLoader(manager).load(url, function (gltf) {
            let boxError = setObject(gltf.scene)
            if (boxError) sizeError();
        }, undefined, function (e) {
            error();
            showCanvasError(e.message)
        });
    } else if (extension === "obj") {
        new THREE.OBJLoader(manager).load(url, function (object) {
                object.traverse(function (child) {
                    if (child instanceof THREE.Mesh) child.material = material
                });
                let boxError = setObject(object)
                if (boxError) sizeError();
            }, undefined, function (e) {
                error();
                showCanvasError(e.message)
            }
        );
    } else if (extension === "stl") {
        new THREE.STLLoader(manager).load(url, function (geometry) {
                const mesh = new THREE.Mesh(geometry, material)
                let boxError = setObject(mesh);
                if (boxError) sizeError();
            }, undefined, (e) => {
                error();
                showCanvasError(e.message)
            }
        );
    }
}

function setObject(object) {
    currentModel = object;
    const box = new THREE.Box3().setFromObject(object);

    // zoom camera according to object size
    const cameraPosition = Math.max(
        box.max.y - box.min.y,
        box.max.z - box.min.z,
        box.max.x - box.min.x
    ) * 0.5;
    camera.position.x = cameraPosition;
    camera.position.y = cameraPosition * 0.5;
    camera.position.z = cameraPosition * 2.5;
    camera.lookAt(0, 0, 0)

    //position object to origin
    object.position.x = -box.min.x - (box.max.x - box.min.x) / 2;
    object.position.y = -box.min.y - (box.max.y - box.min.y) / 2;
    object.position.z = -box.min.z - (box.max.z - box.min.z) / 2;

    scene.add(object);
    renderer.render(scene, camera);

    // check dimensions of object
    const boxSize = box.getSize();
    if (boxSize.x > 200 || boxSize.y > 200 || boxSize.z > 250) return true
}

function removeModel() {
    scene.remove(currentModel);
    renderer.render(scene, camera);
}

function showCanvasError(error) {
    const errorContainer = document.querySelector('.canvas_error_container');
    errorContainer.style.display = "flex";
    errorContainer.firstElementChild.textContent = error;
}

window.addEventListener('resize', () => {
    updateCanvasDimension();
});

function updateCanvasDimension() {
    sizes.width = canvasContainer.clientWidth;
    sizes.height = canvasContainer.clientHeight;

    camera.aspect = sizes.width / sizes.height;
    camera.updateProjectionMatrix();

    renderer.setSize(sizes.width, sizes.height);
    renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));
    render();
}

function render() {
    renderer.render(scene, camera);
    controls.update();
}

initGui();

function initGui() {
    const gui = new dat.GUI({autoPlace: false});
    gui.close();
    gui.domElement.id = "gui";
    canvasContainer.appendChild(gui.domElement);

    const data = {
        sceneColor: scene.background.getHex(),
        materialColor: material.color.getHex()
    };

    const cameraFolder = gui.addFolder("Camera")
    cameraFolder.add(camera.position, "x", 0, 10, 0.01).onChange(render).listen()
    cameraFolder.add(camera.position, "y", 0, 10, 0.01).onChange(render).listen()
    cameraFolder.add(camera.position, "z", 0, 10, 0.01).onChange(render).listen()

    const colorFolder = gui.addFolder("Colors")
    colorFolder.addColor(data, "sceneColor").onChange(() => {
        scene.background.setHex(Number(data.sceneColor.toString().replace('#', '0x')))
        render()
    });
    colorFolder.addColor(data, "materialColor").onChange(() => {
        material.color.setHex(Number(data.materialColor.toString().replace('#', '0x')))
        render()
    });
}

function takeSnapshot() {
    const temporaryCanvas = document.createElement('canvas');
    let context = temporaryCanvas.getContext('2d');

    let cWidth = canvas.width;
    let cHeight = canvas.height;

    if (cWidth < cHeight) {
        temporaryCanvas.width = cWidth;
        temporaryCanvas.height = cWidth;
        let x = cHeight / 2 - cWidth / 2;
        context.drawImage(canvas, 0, x, cWidth, cWidth, 0, 0, cWidth, cWidth);
    } else {
        temporaryCanvas.width = cHeight;
        temporaryCanvas.height = cHeight;
        let x = cWidth / 2 - cHeight / 2;
        context.drawImage(canvas, x, 0, cHeight, cHeight, 0, 0, cHeight, cHeight);
    }
    return temporaryCanvas.toDataURL("image/png");
}