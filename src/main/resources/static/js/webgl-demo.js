var zoom = 1.0;
var mouseDown = false;
var lastMouseX = 0.0;
var lastMouseY = 0.0;
var mouseX = 0.0;
var mouseY = 0.0;
var objectRotationX = 0.0;
var objectRotationY = 0.0;
let staticPositionX = 0.0;
let staticPositionY = 0.0;


const canvas = document.querySelector('#glcanvas');
const gl = canvas.getContext('webgl2');

canvas.addEventListener('mousedown', function(evt) {
	mouseX = evt.clientX - canvas.getBoundingClientRect().left;
	mouseY = evt.clientY - canvas.getBoundingClientRect().top;
	lastMouseX = mouseX;
	lastMouseY = mouseY;

	mouseDown = true;
});

canvas.addEventListener('wheel', evt => {
	evt.preventDefault();
	zoom += evt.deltaY * 0.001;
})

canvas.addEventListener('mousemove', evt => {
	if (mouseDown) {
		mouseX = evt.clientX - canvas.getBoundingClientRect().left;
		mouseY = evt.clientY - canvas.getBoundingClientRect().top;
	}
})

canvas.addEventListener('mouseup', function() {
	mouseDown = false;
	staticPositionX += objectRotationX * 0.01;
	staticPositionY += objectRotationY * 0.01;
	objectRotationX = 0;
	objectRotationY = 0;
});

main();

function main() {
	// If we don't have a GL context, give up now
	if (!gl) {
		alert('Unable to initialize WebGL. Your browser or machine may not support it.');
		return;
	}

	// Vertex shader program
	let vertexShaderRequest = new XMLHttpRequest();
	vertexShaderRequest.open('GET', 'shaders/vertexShader', false);
	vertexShaderRequest.send();
	const vsSource = vertexShaderRequest.responseText;


	// Fragment shader program
	let fragmentShaderRequest = new XMLHttpRequest();
	fragmentShaderRequest.open('GET', 'shaders/fragmentShader', false);
	fragmentShaderRequest.send();

	const fsSource = fragmentShaderRequest.responseText;


	// Initialize a shader program; this is where all the lighting
	// for the vertices and so forth is established.
	const shaderProgram = initShaderProgram(gl, vsSource, fsSource);

	// Collect all the info needed to use the shader program.
	// Look up which attributes our shader program is using
	// for aVertexPosition, aVertexColor and also
	// look up uniform locations.
	const programInfo = {
		program: shaderProgram,
		attribLocations: {
			vertexPosition: gl.getAttribLocation(shaderProgram, 'aVertexPosition'),
			vertexNormal: gl.getAttribLocation(shaderProgram, 'aNormals'),
		},
		uniformLocations: {
			projectionMatrix: gl.getUniformLocation(shaderProgram, 'uProjectionMatrix'),
			modelViewMatrix: gl.getUniformLocation(shaderProgram, 'uModelViewMatrix'),
		},
	};

	// Here's where we call the routine that builds all the
	// objects we'll be drawing.
	const buffers = initBuffers(gl);

	var then = 0;

	// Draw the scene repeatedly
	function render(now) {
		now *= 0.001;  // convert to seconds
		const deltaTime = now - then;
		then = now;

		drawScene(gl, programInfo, buffers, buffers.vertexCount, deltaTime);
		mouseInputs();
		requestAnimationFrame(render);
	}

	requestAnimationFrame(render);
}

//
// initBuffers
//
// Initialize the buffers we'll need. For this demo, we just
// have one object -- a simple three-dimensional cube.
//
function initBuffers(gl) {

	// Create a buffer for the cube's vertex positions.

	const positionBuffer = gl.createBuffer();

	// Select the positionBuffer as the one to apply buffer
	// operations to from here out.

	gl.bindBuffer(gl.ARRAY_BUFFER, positionBuffer);

	// Now create an array of positions for the cube.

	//HTML request the data here. Put this in an array. Print out the array to test.
	let request = new XMLHttpRequest();
	request.open('GET', 'monkey', false);

	request.onload = function() {
		if (request.readyState === request.DONE) {
			return this.responseText;
		}
	};
	console.log('sending');
	request.send();
	let JSONResponse = JSON.parse(request.responseText);
	let importedVertices = [];
	for (let vertex of JSONResponse["Vertices"]) {
		importedVertices.push(vertex[0]);
		importedVertices.push(vertex[1]);
		importedVertices.push(vertex[2]);
	}
	const vCount = JSONResponse["VertexCount"];

	// Now pass the list of positions into WebGL to build the
	// shape. We do this by creating a Float32Array from the
	// JavaScript array, then use it to fill the current buffer.

	gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(importedVertices), gl.STATIC_DRAW);


	return {
		position: positionBuffer,
		vertexCount: vCount
	};
}

//
// Draw the scene.
//
function drawScene(gl, programInfo, buffers, vertexCount, deltaTime) {
	gl.clearColor(1.0, 0.0, 0.0, 1.0);  // Clear to black, fully opaque
	gl.clearDepth(1.0);                 // Clear everything
	gl.enable(gl.DEPTH_TEST);           // Enable depth testing
	gl.depthFunc(gl.LEQUAL);            // Near things obscure far things

	// Clear the canvas before we start drawing on it.

	gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);

	// Create a perspective matrix, a special matrix that is
	// used to simulate the distortion of perspective in a camera.
	// Our field of view is 45 degrees, with a width/height
	// ratio that matches the display size of the canvas
	// and we only want to see objects between 0.1 units
	// and 100 units away from the camera.

	const fieldOfView = (45 * Math.PI / 180) * zoom;   // in radians
	const aspect = gl.canvas.clientWidth / gl.canvas.clientHeight;
	const zNear = 0.1;
	const zFar = 100.0;
	const projectionMatrix = mat4.create();

	// note: glmatrix.js always has the first argument
	// as the destination to receive the result.
	mat4.perspective(projectionMatrix,
		fieldOfView,
		aspect,
		zNear,
		zFar);

	// Set the drawing position to the "identity" point, which is
	// the center of the scene.
	const modelViewMatrix = mat4.create();
	const xRotationMatrix = mat4.create();
	const yRotationMatrix = mat4.create();
	// Now move the drawing position a bit to where we want to
	// start drawing the square.

	mat4.translate(modelViewMatrix,     // destination matrix
		modelViewMatrix,     // matrix to translate
		[-0.0, 0.0, -15.0]);  // amount to translate
	mat4.rotate(xRotationMatrix,  // destination matrix
		xRotationMatrix,  // matrix to rotate
		staticPositionY + objectRotationY * 0.01,     // amount to rotate in radians
		[0, 0, 1]);       // axis to rotate around (Z)
	mat4.rotate(yRotationMatrix,  // destination matrix
		yRotationMatrix,  // matrix to rotate
		staticPositionX + objectRotationX * 0.01,// amount to rotate in radians
		[0, 1, 0]);       // axis to rotate around (X)

	mat4.multiply(yRotationMatrix, yRotationMatrix, xRotationMatrix);
	mat4.multiply(modelViewMatrix, modelViewMatrix, yRotationMatrix);

	// Tell WebGL how to pull out the positions from the position
	// buffer into the vertexPosition attribute
	{
		const numComponents = 3;
		const type = gl.FLOAT;
		const normalize = false;
		const stride = 6 * Float32Array.BYTES_PER_ELEMENT;
		const offset = 0;
		gl.bindBuffer(gl.ARRAY_BUFFER, buffers.position);
		gl.vertexAttribPointer(
			programInfo.attribLocations.vertexPosition,
			numComponents,
			type,
			normalize,
			stride,
			offset);
		gl.enableVertexAttribArray(
			programInfo.attribLocations.vertexPosition);
	}
	//Normals attrib location setup
	{
		const numComponents = 3;
		const type = gl.FLOAT;
		const normalize = false; //Normalized in shader. Should be fine here.
		const stride = 6 * Float32Array.BYTES_PER_ELEMENT;
		const offset = 3 * Float32Array.BYTES_PER_ELEMENT;
		gl.bindBuffer(gl.ARRAY_BUFFER, buffers.position);
		gl.vertexAttribPointer(
			programInfo.attribLocations.vertexNormal,
			numComponents,
			type,
			normalize,
			stride,
			offset);
		gl.enableVertexAttribArray(
			programInfo.attribLocations.vertexNormal);
	}

	// Tell WebGL which indices to use to index the vertices
	gl.bindBuffer(gl.ARRAY_BUFFER, buffers.position);

	// Tell WebGL to use our program when drawing

	gl.useProgram(programInfo.program);

	// Set the shader uniforms

	gl.uniformMatrix4fv(
		programInfo.uniformLocations.projectionMatrix,
		false,
		projectionMatrix);
	gl.uniformMatrix4fv(
		programInfo.uniformLocations.modelViewMatrix,
		false,
		modelViewMatrix);

	{
		const type = gl.UNSIGNED_SHORT;
		const offset = 0;
		gl.drawArrays(gl.TRIANGLES, offset, vertexCount);
	}

	// Update the rotation for the next draw

	//cubeRotation += deltaTime;
}

//
// Initialize a shader program, so WebGL knows how to draw our data
//
function initShaderProgram(gl, vsSource, fsSource) {
	const vertexShader = loadShader(gl, gl.VERTEX_SHADER, vsSource);
	const fragmentShader = loadShader(gl, gl.FRAGMENT_SHADER, fsSource);

	// Create the shader program

	const shaderProgram = gl.createProgram();
	gl.attachShader(shaderProgram, vertexShader);
	gl.attachShader(shaderProgram, fragmentShader);
	gl.linkProgram(shaderProgram);

	// If creating the shader program failed, alert

	if (!gl.getProgramParameter(shaderProgram, gl.LINK_STATUS)) {
		alert('Unable to initialize the shader program: ' + gl.getProgramInfoLog(shaderProgram));
		return null;
	}

	return shaderProgram;
}

//
// creates a shader of the given type, uploads the source and
// compiles it.
//
function loadShader(gl, type, source) {
	const shader = gl.createShader(type);

	// Send the source to the shader object

	gl.shaderSource(shader, source);

	// Compile the shader program

	gl.compileShader(shader);

	// See if it compiled successfully

	if (!gl.getShaderParameter(shader, gl.COMPILE_STATUS)) {
		alert('An error occurred compiling the shaders: ' + gl.getShaderInfoLog(shader));
		gl.deleteShader(shader);
		return null;
	}

	return shader;
}


function mouseInputs() {

	if (mouseDown) {
		objectRotationX = mouseX - lastMouseX;
		objectRotationY = mouseY - lastMouseY;
	}


}