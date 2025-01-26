precision highp float;

uniform vec2 iResolution;
uniform float iTime;

void main() {
//    float threshold = 256.0f;
//    float threshold2 = threshold * threshold;
//
//    float centralX = -0.5f;
//    float centralY = 0.0f;
//    float sizeX = 2.0f;
//
//    int iterationsLimit = 256;
//
//    float i = gl_FragCoord.x;
//    float j = gl_FragCoord.y;
//    float sizeY = sizeX * iResolution.y / iResolution.x;
//
//    float fromX = centralX - sizeX / 2.0f;
//    float fromY = centralY - sizeY / 2.0f;
//
//    float x0 = fromX + i * sizeX / iResolution.x;
//    float y0 = fromY + j * sizeY / iResolution.y;
//
//    float x = x0;
//    float y = y0;
//
//    int iter = 0;
//    for (; iter < iterationsLimit; ++iter) {
//        float xPrev = x;
//        x = x * x - y * y + x0;
//        y = 2.0f * xPrev * y + y0;
//        if ((x * x + y * y) > threshold2) {
//            break;
//        }
//    }
//    float result = float(iter);
////    if (smoothing && iter != iterationsLimit) {
////        result = result - log(log(sqrt(x * x + y * y)) / log(threshold)) / log(2.0f);
////    }
//
//    result = result / float(iterationsLimit);
//
//    vec3 a = vec3(0.5, 0.5, 0.5);
//    vec3 b = vec3(0.5, 0.5, 0.5);
//    vec3 c = vec3(1.0, 0.7, 0.4);
//    vec3 d = vec3(0.00, 0.15, 0.20);
//    vec3 color = a + b * cos(2.0f * 3.14f * (c * result + d));

//    float iTime = 10.0;

    vec2 p = vec2(-.745,.186) + 3.*(gl_FragCoord.xy/iResolution.y-.5)*pow(.01,1.+cos(.2*iTime));

    float n = 0.;
    vec2 z = p*n;

    for( ; n<128. && dot(z,z)<1e4; n++ )
    z = vec2( z.x*z.x - z.y*z.y, 2.*z.x*z.y ) + p;

    gl_FragColor = .5 + .5*cos( vec4(3,4,11,0) + .05*(n - log2(log2(dot(z,z)))) );

//    float N = 100.0;
//    float iTime = 0.0;
//    float t = iTime/2.,           // rotation at scaling
//    st = 2.*exp(-6.*(1.-cos(.1*t))),
//    c = cos(t),s = sin(t);
//    mat2 M = mat2(c,-s,s,c);
//
//    vec2 R = iResolution.xy,            // coordinates transform
//    z0 = st * M * (gl_FragCoord.xy/R.y - vec2(1.1,.5)) -vec2(.4615,-.622),
//    z = z0,
//    m = z-z;
//
//    vec4 o;
//    for (float i=0.; i<N; i++) {
//        if (dot(z,z)>4.) { o = vec4(1.-i/N); return;} // z diverged
//        z -= m;
//        z = z0+m + mat2(z,-z.y,z.x)*z;
//    }
//    gl_FragColor = o;

//    gl_FragColor = vec4(result, 0.0, 0.0, 1.0);
}
