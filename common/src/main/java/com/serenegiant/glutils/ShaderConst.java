/*     */ package com.serenegiant.glutils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ShaderConst
/*     */ {
/*     */   public static final int GL_TEXTURE_EXTERNAL_OES = 36197;
/*     */   public static final int GL_TEXTURE_2D = 3553;
/*     */   public static final String SHADER_VERSION = "#version 100\n";
/*     */   public static final String HEADER_2D = "";
/*     */   public static final String SAMPLER_2D = "sampler2D";
/*     */   public static final String HEADER_OES = "#extension GL_OES_EGL_image_external : require\n";
/*     */   public static final String SAMPLER_OES = "samplerExternalOES";
/*     */   public static final int KERNEL_SIZE3x3 = 9;
/*     */   public static final int KERNEL_SIZE5x５ = 25;
/*     */   public static final String FUNC_RGB2HSV = "vec3 rgb2hsv(vec3 c) {\nvec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);\nvec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));\nvec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));\nfloat d = q.x - min(q.w, q.y);\nfloat e = 1.0e-10;\nreturn vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);\n}\n";
/*     */   public static final String FUNC_HSV2RGB = "vec3 hsv2rgb(vec3 c) {\nvec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);\nvec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);\nreturn c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);\n}\n";
/*     */   public static final String FUNC_GET_INTENSITY = "const highp vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);\nhighp float getIntensity(vec3 c) {\nreturn dot(c.rgb, luminanceWeighting);\n}\n";
/*     */   public static final String VERTEX_SHADER = "#version 100\nuniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute highp vec4 aPosition;\nattribute highp vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n";
/*     */   public static final String FRAGMENT_SHADER_SIMPLE_OES = "#version 100\n#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nuniform samplerExternalOES sTexture;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n  gl_FragColor = texture2D(sTexture, vTextureCoord);\n}";
/*     */   public static final String FRAGMENT_SHADER_SIMPLE = "#version 100\nprecision mediump float;\nuniform sampler2D sTexture;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n  gl_FragColor = texture2D(sTexture, vTextureCoord);\n}";
/*     */   private static final String FRAGMENT_SHADER_BASE = "#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nvoid main() {\n    gl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n";
/* 123 */   public static final String FRAGMENT_SHADER_2D = String.format("#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nvoid main() {\n    gl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n", new Object[] { "", "sampler2D" });
/*     */   
/* 125 */   public static final String FRAGMENT_SHADER_EXT = String.format("#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nvoid main() {\n    gl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n", new Object[] { "#extension GL_OES_EGL_image_external : require\n", "samplerExternalOES" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String FRAGMENT_SHADER_BW_BASE = "#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nvoid main() {\n    vec4 tc = texture2D(sTexture, vTextureCoord);\n    float color = tc.r * 0.3 + tc.g * 0.59 + tc.b * 0.11;\n    gl_FragColor = vec4(color, color, color, 1.0);\n}\n";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 139 */   public static final String FRAGMENT_SHADER_BW = String.format("#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nvoid main() {\n    vec4 tc = texture2D(sTexture, vTextureCoord);\n    float color = tc.r * 0.3 + tc.g * 0.59 + tc.b * 0.11;\n    gl_FragColor = vec4(color, color, color, 1.0);\n}\n", new Object[] { "", "sampler2D" });
/*     */   
/* 141 */   public static final String FRAGMENT_SHADER_EXT_BW = String.format("#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nvoid main() {\n    vec4 tc = texture2D(sTexture, vTextureCoord);\n    float color = tc.r * 0.3 + tc.g * 0.59 + tc.b * 0.11;\n    gl_FragColor = vec4(color, color, color, 1.0);\n}\n", new Object[] { "#extension GL_OES_EGL_image_external : require\n", "samplerExternalOES" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String FRAGMENT_SHADER_NIGHT_BASE = "#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nvoid main() {\n    vec4 tc = texture2D(sTexture, vTextureCoord);\n    float color = ((tc.r * 0.3 + tc.g * 0.59 + tc.b * 0.11) - 0.5 * 1.5) + 0.8;\n    gl_FragColor = vec4(color, color + 0.15, color, 1.0);\n}\n";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 155 */   public static final String FRAGMENT_SHADER_NIGHT = String.format("#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nvoid main() {\n    vec4 tc = texture2D(sTexture, vTextureCoord);\n    float color = ((tc.r * 0.3 + tc.g * 0.59 + tc.b * 0.11) - 0.5 * 1.5) + 0.8;\n    gl_FragColor = vec4(color, color + 0.15, color, 1.0);\n}\n", new Object[] { "", "sampler2D" });
/*     */   
/* 157 */   public static final String FRAGMENT_SHADER_EXT_NIGHT = String.format("#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nvoid main() {\n    vec4 tc = texture2D(sTexture, vTextureCoord);\n    float color = ((tc.r * 0.3 + tc.g * 0.59 + tc.b * 0.11) - 0.5 * 1.5) + 0.8;\n    gl_FragColor = vec4(color, color + 0.15, color, 1.0);\n}\n", new Object[] { "#extension GL_OES_EGL_image_external : require\n", "samplerExternalOES" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String FRAGMENT_SHADER_CHROMA_KEY_BASE = "#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nvoid main() {\n    vec4 tc = texture2D(sTexture, vTextureCoord);\n    float color = ((tc.r * 0.3 + tc.g * 0.59 + tc.b * 0.11) - 0.5 * 1.5) + 0.8;\n    if(tc.g > 0.6 && tc.b < 0.6 && tc.r < 0.6){ \n        gl_FragColor = vec4(0, 0, 0, 0.0);\n    }else{ \n        gl_FragColor = texture2D(sTexture, vTextureCoord);\n    }\n}\n";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 175 */   public static final String FRAGMENT_SHADER_CHROMA_KEY = String.format("#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nvoid main() {\n    vec4 tc = texture2D(sTexture, vTextureCoord);\n    float color = ((tc.r * 0.3 + tc.g * 0.59 + tc.b * 0.11) - 0.5 * 1.5) + 0.8;\n    if(tc.g > 0.6 && tc.b < 0.6 && tc.r < 0.6){ \n        gl_FragColor = vec4(0, 0, 0, 0.0);\n    }else{ \n        gl_FragColor = texture2D(sTexture, vTextureCoord);\n    }\n}\n", new Object[] { "", "sampler2D" });
/*     */   
/* 177 */   public static final String FRAGMENT_SHADER_EXT_CHROMA_KEY = String.format("#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nvoid main() {\n    vec4 tc = texture2D(sTexture, vTextureCoord);\n    float color = ((tc.r * 0.3 + tc.g * 0.59 + tc.b * 0.11) - 0.5 * 1.5) + 0.8;\n    if(tc.g > 0.6 && tc.b < 0.6 && tc.r < 0.6){ \n        gl_FragColor = vec4(0, 0, 0, 0.0);\n    }else{ \n        gl_FragColor = texture2D(sTexture, vTextureCoord);\n    }\n}\n", new Object[] { "#extension GL_OES_EGL_image_external : require\n", "samplerExternalOES" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String FRAGMENT_SHADER_SQUEEZE_BASE = "#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nuniform vec2 uPosition;\nvoid main() {\n    vec2 texCoord = vTextureCoord.xy;\n    vec2 normCoord = 2.0 * texCoord - 1.0;\n    float r = length(normCoord); // to polar coords \n    float phi = atan(normCoord.y + uPosition.y, normCoord.x + uPosition.x); // to polar coords \n    r = pow(r, 1.0/1.8) * 0.8;\n    normCoord.x = r * cos(phi); \n    normCoord.y = r * sin(phi); \n    texCoord = normCoord / 2.0 + 0.5;\n    gl_FragColor = texture2D(sTexture, texCoord);\n}\n";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 197 */   public static final String FRAGMENT_SHADER_SQUEEZE = String.format("#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nuniform vec2 uPosition;\nvoid main() {\n    vec2 texCoord = vTextureCoord.xy;\n    vec2 normCoord = 2.0 * texCoord - 1.0;\n    float r = length(normCoord); // to polar coords \n    float phi = atan(normCoord.y + uPosition.y, normCoord.x + uPosition.x); // to polar coords \n    r = pow(r, 1.0/1.8) * 0.8;\n    normCoord.x = r * cos(phi); \n    normCoord.y = r * sin(phi); \n    texCoord = normCoord / 2.0 + 0.5;\n    gl_FragColor = texture2D(sTexture, texCoord);\n}\n", new Object[] { "", "sampler2D" });
/*     */   
/* 199 */   public static final String FRAGMENT_SHADER_EXT_SQUEEZE = String.format("#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nuniform vec2 uPosition;\nvoid main() {\n    vec2 texCoord = vTextureCoord.xy;\n    vec2 normCoord = 2.0 * texCoord - 1.0;\n    float r = length(normCoord); // to polar coords \n    float phi = atan(normCoord.y + uPosition.y, normCoord.x + uPosition.x); // to polar coords \n    r = pow(r, 1.0/1.8) * 0.8;\n    normCoord.x = r * cos(phi); \n    normCoord.y = r * sin(phi); \n    texCoord = normCoord / 2.0 + 0.5;\n    gl_FragColor = texture2D(sTexture, texCoord);\n}\n", new Object[] { "#extension GL_OES_EGL_image_external : require\n", "samplerExternalOES" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String FRAGMENT_SHADER_EXT_TWIRL = "#version 100\n#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nuniform vec2 uPosition;\nvoid main() {\n    vec2 texCoord = vTextureCoord.xy;\n    vec2 normCoord = 2.0 * texCoord - 1.0;\n    float r = length(normCoord); // to polar coords \n    float phi = atan(normCoord.y + uPosition.y, normCoord.x + uPosition.x); // to polar coords \n    phi = phi + (1.0 - smoothstep(-0.5, 0.5, r)) * 4.0;\n    normCoord.x = r * cos(phi); \n    normCoord.y = r * sin(phi); \n    texCoord = normCoord / 2.0 + 0.5;\n    gl_FragColor = texture2D(sTexture, texCoord);\n}\n";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String FRAGMENT_SHADER_EXT_TUNNEL = "#version 100\n#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nuniform vec2 uPosition;\nvoid main() {\n    vec2 texCoord = vTextureCoord.xy;\n    vec2 normCoord = 2.0 * texCoord - 1.0;\n    float r = length(normCoord); // to polar coords \n    float phi = atan(normCoord.y + uPosition.y, normCoord.x + uPosition.x); // to polar coords \n    if (r > 0.5) r = 0.5;\n    normCoord.x = r * cos(phi); \n    normCoord.y = r * sin(phi); \n    texCoord = normCoord / 2.0 + 0.5;\n    gl_FragColor = texture2D(sTexture, texCoord);\n}\n";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String FRAGMENT_SHADER_EXT_BULGE = "#version 100\n#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nuniform vec2 uPosition;\nvoid main() {\n    vec2 texCoord = vTextureCoord.xy;\n    vec2 normCoord = 2.0 * texCoord - 1.0;\n    float r = length(normCoord); // to polar coords \n    float phi = atan(normCoord.y + uPosition.y, normCoord.x + uPosition.x); // to polar coords \n    r = r * smoothstep(-0.1, 0.5, r);\n    normCoord.x = r * cos(phi); \n    normCoord.y = r * sin(phi); \n    texCoord = normCoord / 2.0 + 0.5;\n    gl_FragColor = texture2D(sTexture, texCoord);\n}\n";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String FRAGMENT_SHADER_EXT_DENT = "#version 100\n#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nuniform vec2 uPosition;\nvoid main() {\n    vec2 texCoord = vTextureCoord.xy;\n    vec2 normCoord = 2.0 * texCoord - 1.0;\n    float r = length(normCoord); // to polar coords \n    float phi = atan(normCoord.y + uPosition.y, normCoord.x + uPosition.x); // to polar coords \n    r = 2.0 * r - r * smoothstep(0.0, 0.7, r);\n    normCoord.x = r * cos(phi); \n    normCoord.y = r * sin(phi); \n    texCoord = normCoord / 2.0 + 0.5;\n    gl_FragColor = texture2D(sTexture, texCoord);\n}\n";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String FRAGMENT_SHADER_EXT_FISHEYE = "#version 100\n#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nuniform vec2 uPosition;\nvoid main() {\n    vec2 texCoord = vTextureCoord.xy;\n    vec2 normCoord = 2.0 * texCoord - 1.0;\n    float r = length(normCoord); // to polar coords \n    float phi = atan(normCoord.y + uPosition.y, normCoord.x + uPosition.x); // to polar coords \n    r = r * r / sqrt(2.0);\n    normCoord.x = r * cos(phi); \n    normCoord.y = r * sin(phi); \n    texCoord = normCoord / 2.0 + 0.5;\n    gl_FragColor = texture2D(sTexture, texCoord);\n}\n";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String FRAGMENT_SHADER_EXT_STRETCH = "#version 100\n#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nuniform vec2 uPosition;\nvoid main() {\n    vec2 texCoord = vTextureCoord.xy;\n    vec2 normCoord = 2.0 * texCoord - 1.0;\n    vec2 s = sign(normCoord + uPosition);\n    normCoord = abs(normCoord);\n    normCoord = 0.5 * normCoord + 0.5 * smoothstep(0.25, 0.5, normCoord) * normCoord;\n    normCoord = s * normCoord;\n    texCoord = normCoord / 2.0 + 0.5;\n    gl_FragColor = texture2D(sTexture, texCoord);\n}\n";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String FRAGMENT_SHADER_EXT_MIRROR = "#version 100\n#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nuniform vec2 uPosition;\nvoid main() {\n    vec2 texCoord = vTextureCoord.xy;\n    vec2 normCoord = 2.0 * texCoord - 1.0;\n    normCoord.x = normCoord.x * sign(normCoord.x + uPosition.x);\n    texCoord = normCoord / 2.0 + 0.5;\n    gl_FragColor = texture2D(sTexture, texCoord);\n}\n";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String FRAGMENT_SHADER_SOBEL_BASE = "#version 100\n%s#define KERNEL_SIZE3x3 9\nprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uKernel[18];\nuniform vec2  uTexOffset[KERNEL_SIZE3x3];\nuniform float uColorAdjust;\nvoid main() {\n    vec3 t0 = texture2D(sTexture, vTextureCoord + uTexOffset[0]).rgb;\n    vec3 t1 = texture2D(sTexture, vTextureCoord + uTexOffset[1]).rgb;\n    vec3 t2 = texture2D(sTexture, vTextureCoord + uTexOffset[2]).rgb;\n    vec3 t3 = texture2D(sTexture, vTextureCoord + uTexOffset[3]).rgb;\n    vec3 t4 = texture2D(sTexture, vTextureCoord + uTexOffset[4]).rgb;\n    vec3 t5 = texture2D(sTexture, vTextureCoord + uTexOffset[5]).rgb;\n    vec3 t6 = texture2D(sTexture, vTextureCoord + uTexOffset[6]).rgb;\n    vec3 t7 = texture2D(sTexture, vTextureCoord + uTexOffset[7]).rgb;\n    vec3 t8 = texture2D(sTexture, vTextureCoord + uTexOffset[8]).rgb;\n    vec3 sumH = t0 * uKernel[0] + t1 * uKernel[1] + t2 * uKernel[2]\n              + t3 * uKernel[3] + t4 * uKernel[4] + t5 * uKernel[5]\n              + t6 * uKernel[6] + t7 * uKernel[7] + t8 * uKernel[8];\n    float mag = length(sumH);\n    gl_FragColor = vec4(vec3(mag), 1.0);\n}\n";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 354 */   public static final String FRAGMENT_SHADER_SOBEL = String.format("#version 100\n%s#define KERNEL_SIZE3x3 9\nprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uKernel[18];\nuniform vec2  uTexOffset[KERNEL_SIZE3x3];\nuniform float uColorAdjust;\nvoid main() {\n    vec3 t0 = texture2D(sTexture, vTextureCoord + uTexOffset[0]).rgb;\n    vec3 t1 = texture2D(sTexture, vTextureCoord + uTexOffset[1]).rgb;\n    vec3 t2 = texture2D(sTexture, vTextureCoord + uTexOffset[2]).rgb;\n    vec3 t3 = texture2D(sTexture, vTextureCoord + uTexOffset[3]).rgb;\n    vec3 t4 = texture2D(sTexture, vTextureCoord + uTexOffset[4]).rgb;\n    vec3 t5 = texture2D(sTexture, vTextureCoord + uTexOffset[5]).rgb;\n    vec3 t6 = texture2D(sTexture, vTextureCoord + uTexOffset[6]).rgb;\n    vec3 t7 = texture2D(sTexture, vTextureCoord + uTexOffset[7]).rgb;\n    vec3 t8 = texture2D(sTexture, vTextureCoord + uTexOffset[8]).rgb;\n    vec3 sumH = t0 * uKernel[0] + t1 * uKernel[1] + t2 * uKernel[2]\n              + t3 * uKernel[3] + t4 * uKernel[4] + t5 * uKernel[5]\n              + t6 * uKernel[6] + t7 * uKernel[7] + t8 * uKernel[8];\n    float mag = length(sumH);\n    gl_FragColor = vec4(vec3(mag), 1.0);\n}\n", new Object[] { "", "sampler2D" });
/*     */   
/* 356 */   public static final String FRAGMENT_SHADER_EXT_SOBEL = String.format("#version 100\n%s#define KERNEL_SIZE3x3 9\nprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uKernel[18];\nuniform vec2  uTexOffset[KERNEL_SIZE3x3];\nuniform float uColorAdjust;\nvoid main() {\n    vec3 t0 = texture2D(sTexture, vTextureCoord + uTexOffset[0]).rgb;\n    vec3 t1 = texture2D(sTexture, vTextureCoord + uTexOffset[1]).rgb;\n    vec3 t2 = texture2D(sTexture, vTextureCoord + uTexOffset[2]).rgb;\n    vec3 t3 = texture2D(sTexture, vTextureCoord + uTexOffset[3]).rgb;\n    vec3 t4 = texture2D(sTexture, vTextureCoord + uTexOffset[4]).rgb;\n    vec3 t5 = texture2D(sTexture, vTextureCoord + uTexOffset[5]).rgb;\n    vec3 t6 = texture2D(sTexture, vTextureCoord + uTexOffset[6]).rgb;\n    vec3 t7 = texture2D(sTexture, vTextureCoord + uTexOffset[7]).rgb;\n    vec3 t8 = texture2D(sTexture, vTextureCoord + uTexOffset[8]).rgb;\n    vec3 sumH = t0 * uKernel[0] + t1 * uKernel[1] + t2 * uKernel[2]\n              + t3 * uKernel[3] + t4 * uKernel[4] + t5 * uKernel[5]\n              + t6 * uKernel[6] + t7 * uKernel[7] + t8 * uKernel[8];\n    float mag = length(sumH);\n    gl_FragColor = vec4(vec3(mag), 1.0);\n}\n", new Object[] { "#extension GL_OES_EGL_image_external : require\n", "samplerExternalOES" });
/*     */   
/* 358 */   public static final float[] KERNEL_NULL = new float[] { 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F };
/* 359 */   public static final float[] KERNEL_SOBEL_H = new float[] { 1.0F, 0.0F, -1.0F, 2.0F, 0.0F, -2.0F, 1.0F, 0.0F, -1.0F };
/* 360 */   public static final float[] KERNEL_SOBEL_V = new float[] { 1.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F, -1.0F, -2.0F, -1.0F };
/* 361 */   public static final float[] KERNEL_SOBEL2_H = new float[] { 3.0F, 0.0F, -3.0F, 10.0F, 0.0F, -10.0F, 3.0F, 0.0F, -3.0F };
/* 362 */   public static final float[] KERNEL_SOBEL2_V = new float[] { 3.0F, 10.0F, 3.0F, 0.0F, 0.0F, 0.0F, -3.0F, -10.0F, -3.0F };
/* 363 */   public static final float[] KERNEL_SHARPNESS = new float[] { 0.0F, -1.0F, 0.0F, -1.0F, 5.0F, -1.0F, 0.0F, -1.0F, 0.0F };
/* 364 */   public static final float[] KERNEL_EDGE_DETECT = new float[] { -1.0F, -1.0F, -1.0F, -1.0F, 8.0F, -1.0F, -1.0F, -1.0F, -1.0F };
/* 365 */   public static final float[] KERNEL_EMBOSS = new float[] { 2.0F, 0.0F, 0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F, -1.0F };
/* 366 */   public static final float[] KERNEL_SMOOTH = new float[] { 0.11111111F, 0.11111111F, 0.11111111F, 0.11111111F, 0.11111111F, 0.11111111F, 0.11111111F, 0.11111111F, 0.11111111F };
/* 367 */   public static final float[] KERNEL_GAUSSIAN = new float[] { 0.0625F, 0.125F, 0.0625F, 0.125F, 0.25F, 0.125F, 0.0625F, 0.125F, 0.0625F };
/* 368 */   public static final float[] KERNEL_BRIGHTEN = new float[] { 1.0F, 1.0F, 1.0F, 1.0F, 2.0F, 1.0F, 1.0F, 1.0F, 1.0F };
/* 369 */   public static final float[] KERNEL_LAPLACIAN = new float[] { 1.0F, 1.0F, 1.0F, 1.0F, -8.0F, 1.0F, 1.0F, 1.0F, 1.0F };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String FRAGMENT_SHADER_FILT3x3_BASE = "#version 100\n%s#define KERNEL_SIZE3x3 9\nprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uKernel[18];\nuniform vec2  uTexOffset[KERNEL_SIZE3x3];\nuniform float uColorAdjust;\nvoid main() {\n    vec4 sum = vec4(0.0);\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[0]) * uKernel[0];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[1]) * uKernel[1];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[2]) * uKernel[2];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[3]) * uKernel[3];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[4]) * uKernel[4];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[5]) * uKernel[5];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[6]) * uKernel[6];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[7]) * uKernel[7];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[8]) * uKernel[8];\n    gl_FragColor = sum + uColorAdjust;\n}\n";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 394 */   public static final String FRAGMENT_SHADER_FILT3x3 = String.format("#version 100\n%s#define KERNEL_SIZE3x3 9\nprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uKernel[18];\nuniform vec2  uTexOffset[KERNEL_SIZE3x3];\nuniform float uColorAdjust;\nvoid main() {\n    vec4 sum = vec4(0.0);\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[0]) * uKernel[0];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[1]) * uKernel[1];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[2]) * uKernel[2];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[3]) * uKernel[3];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[4]) * uKernel[4];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[5]) * uKernel[5];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[6]) * uKernel[6];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[7]) * uKernel[7];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[8]) * uKernel[8];\n    gl_FragColor = sum + uColorAdjust;\n}\n", new Object[] { "", "sampler2D" });
/*     */   
/* 396 */   public static final String FRAGMENT_SHADER_EXT_FILT3x3 = String.format("#version 100\n%s#define KERNEL_SIZE3x3 9\nprecision highp float;\nvarying       vec2 vTextureCoord;\nuniform %s    sTexture;\nuniform float uKernel[18];\nuniform vec2  uTexOffset[KERNEL_SIZE3x3];\nuniform float uColorAdjust;\nvoid main() {\n    vec4 sum = vec4(0.0);\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[0]) * uKernel[0];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[1]) * uKernel[1];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[2]) * uKernel[2];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[3]) * uKernel[3];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[4]) * uKernel[4];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[5]) * uKernel[5];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[6]) * uKernel[6];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[7]) * uKernel[7];\n    sum += texture2D(sTexture, vTextureCoord + uTexOffset[8]) * uKernel[8];\n    gl_FragColor = sum + uColorAdjust;\n}\n", new Object[] { "#extension GL_OES_EGL_image_external : require\n", "samplerExternalOES" });
/*     */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\glutils\ShaderConst.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */