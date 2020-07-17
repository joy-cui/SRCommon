/*      */ package com.serenegiant.glutils;
/*      */ 
/*      */ import android.annotation.SuppressLint;
/*      */ import android.graphics.Bitmap;
/*      */ import android.graphics.SurfaceTexture;
/*      */ import android.opengl.GLES20;
/*      */ import android.support.annotation.Nullable;
/*      */ import android.util.Log;
/*      */ import android.util.SparseArray;
/*      */ import android.view.Surface;
/*      */ import com.serenegiant.utils.BuildCheck;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class EffectRendererHolder
/*      */   implements IRendererHolder
/*      */ {
/*   52 */   private static final String TAG = EffectRendererHolder.class.getSimpleName();
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int MAX_PARAM_NUM = 18;
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int EFFECT_NON = 0;
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int EFFECT_GRAY = 1;
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int EFFECT_GRAY_REVERSE = 2;
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int EFFECT_BIN = 3;
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int EFFECT_BIN_YELLOW = 4;
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int EFFECT_BIN_GREEN = 5;
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int EFFECT_BIN_REVERSE = 6;
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int EFFECT_BIN_REVERSE_YELLOW = 7;
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int EFFECT_BIN_REVERSE_GREEN = 8;
/*      */ 
/*      */   
/*      */   public static final int EFFECT_EMPHASIZE_RED_YELLOW = 9;
/*      */ 
/*      */   
/*      */   public static final int EFFECT_EMPHASIZE_RED_YELLOW_WHITE = 10;
/*      */ 
/*      */   
/*      */   public static final int EFFECT_NUM = 11;
/*      */ 
/*      */   
/*      */   private static final String FRAGMENT_SHADER_GRAY_BASE = "#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nconst vec3 conv = vec3(0.3, 0.59, 0.11);\nvoid main() {\n    vec4 tc = texture2D(sTexture, vTextureCoord);\n    float color = dot(tc.rgb, conv);\n    vec3 cl3 = vec3(color, color, color);\n    gl_FragColor = vec4(cl3, 1.0);\n}\n";
/*      */ 
/*      */   
/*  107 */   private static final String FRAGMENT_SHADER_GRAY_OES = String.format("#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nconst vec3 conv = vec3(0.3, 0.59, 0.11);\nvoid main() {\n    vec4 tc = texture2D(sTexture, vTextureCoord);\n    float color = dot(tc.rgb, conv);\n    vec3 cl3 = vec3(color, color, color);\n    gl_FragColor = vec4(cl3, 1.0);\n}\n", new Object[] { "#extension GL_OES_EGL_image_external : require\n", "samplerExternalOES" });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String FRAGMENT_SHADER_GRAY_REVERSE_BASE = "#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nconst vec3 conv = vec3(0.3, 0.59, 0.11);\nvoid main() {\n    vec4 tc = texture2D(sTexture, vTextureCoord);\n    float color = dot(tc.rgb, conv);\n    vec3 cl3 = vec3(color, color, color);\n    gl_FragColor = vec4(clamp(vec3(1.0, 1.0, 1.0) - cl3, 0.0, 1.0), 1.0);\n}\n";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  127 */   private static final String FRAGMENT_SHADER_GRAY_REVERSE_OES = String.format("#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nconst vec3 conv = vec3(0.3, 0.59, 0.11);\nvoid main() {\n    vec4 tc = texture2D(sTexture, vTextureCoord);\n    float color = dot(tc.rgb, conv);\n    vec3 cl3 = vec3(color, color, color);\n    gl_FragColor = vec4(clamp(vec3(1.0, 1.0, 1.0) - cl3, 0.0, 1.0), 1.0);\n}\n", new Object[] { "#extension GL_OES_EGL_image_external : require\n", "samplerExternalOES" });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String FRAGMENT_SHADER_BIN_BASE = "#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nconst vec3 conv = vec3(0.3, 0.59, 0.11);\nconst vec3 cl = vec3(%s);\nvoid main() {\n    vec4 tc = texture2D(sTexture, vTextureCoord);\n    float color = dot(tc.rgb, conv);\n    vec3 bin = step(0.3, vec3(color, color, color));\n    gl_FragColor = vec4(cl * bin, 1.0);\n}\n";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  149 */   private static final String FRAGMENT_SHADER_BIN_OES = String.format("#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nconst vec3 conv = vec3(0.3, 0.59, 0.11);\nconst vec3 cl = vec3(%s);\nvoid main() {\n    vec4 tc = texture2D(sTexture, vTextureCoord);\n    float color = dot(tc.rgb, conv);\n    vec3 bin = step(0.3, vec3(color, color, color));\n    gl_FragColor = vec4(cl * bin, 1.0);\n}\n", new Object[] { "#extension GL_OES_EGL_image_external : require\n", "samplerExternalOES", "1.0, 1.0, 1.0" });
/*      */ 
/*      */   
/*  152 */   private static final String FRAGMENT_SHADER_BIN_YELLOW_OES = String.format("#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nconst vec3 conv = vec3(0.3, 0.59, 0.11);\nconst vec3 cl = vec3(%s);\nvoid main() {\n    vec4 tc = texture2D(sTexture, vTextureCoord);\n    float color = dot(tc.rgb, conv);\n    vec3 bin = step(0.3, vec3(color, color, color));\n    gl_FragColor = vec4(cl * bin, 1.0);\n}\n", new Object[] { "#extension GL_OES_EGL_image_external : require\n", "samplerExternalOES", "1.0, 1.0, 0.0" });
/*      */ 
/*      */   
/*  155 */   private static final String FRAGMENT_SHADER_BIN_GREEN_OES = String.format("#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nconst vec3 conv = vec3(0.3, 0.59, 0.11);\nconst vec3 cl = vec3(%s);\nvoid main() {\n    vec4 tc = texture2D(sTexture, vTextureCoord);\n    float color = dot(tc.rgb, conv);\n    vec3 bin = step(0.3, vec3(color, color, color));\n    gl_FragColor = vec4(cl * bin, 1.0);\n}\n", new Object[] { "#extension GL_OES_EGL_image_external : require\n", "samplerExternalOES", "0.0, 1.0, 0.0" });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String FRAGMENT_SHADER_BIN_REVERSE_BASE = "#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nconst vec3 conv = vec3(0.3, 0.59, 0.11);\nconst vec3 cl = vec3(%s);\nvoid main() {\n    vec4 tc = texture2D(sTexture, vTextureCoord);\n    float color = dot(tc.rgb, conv);\n    vec3 bin = step(0.3, vec3(color, color, color));\n    gl_FragColor = vec4(cl * (vec3(1.0, 1.0, 1.0) - bin), 1.0);\n}\n";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  177 */   private static final String FRAGMENT_SHADER_BIN_REVERSE_OES = String.format("#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nconst vec3 conv = vec3(0.3, 0.59, 0.11);\nconst vec3 cl = vec3(%s);\nvoid main() {\n    vec4 tc = texture2D(sTexture, vTextureCoord);\n    float color = dot(tc.rgb, conv);\n    vec3 bin = step(0.3, vec3(color, color, color));\n    gl_FragColor = vec4(cl * (vec3(1.0, 1.0, 1.0) - bin), 1.0);\n}\n", new Object[] { "#extension GL_OES_EGL_image_external : require\n", "samplerExternalOES", "1.0, 1.0, 1.0" });
/*      */ 
/*      */   
/*  180 */   private static final String FRAGMENT_SHADER_BIN_REVERSE_YELLOW_OES = String.format("#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nconst vec3 conv = vec3(0.3, 0.59, 0.11);\nconst vec3 cl = vec3(%s);\nvoid main() {\n    vec4 tc = texture2D(sTexture, vTextureCoord);\n    float color = dot(tc.rgb, conv);\n    vec3 bin = step(0.3, vec3(color, color, color));\n    gl_FragColor = vec4(cl * (vec3(1.0, 1.0, 1.0) - bin), 1.0);\n}\n", new Object[] { "#extension GL_OES_EGL_image_external : require\n", "samplerExternalOES", "1.0, 1.0, 0.0" });
/*      */ 
/*      */   
/*  183 */   private static final String FRAGMENT_SHADER_BIN_REVERSE_GREEN_OES = String.format("#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nconst vec3 conv = vec3(0.3, 0.59, 0.11);\nconst vec3 cl = vec3(%s);\nvoid main() {\n    vec4 tc = texture2D(sTexture, vTextureCoord);\n    float color = dot(tc.rgb, conv);\n    vec3 bin = step(0.3, vec3(color, color, color));\n    gl_FragColor = vec4(cl * (vec3(1.0, 1.0, 1.0) - bin), 1.0);\n}\n", new Object[] { "#extension GL_OES_EGL_image_external : require\n", "samplerExternalOES", "0.0, 1.0, 0.0" });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String FRAGMENT_SHADER_EMPHASIZE_RED_YELLOW_BASE = "#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nuniform float uParams[18];\nvec3 rgb2hsv(vec3 c) {\nvec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);\nvec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));\nvec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));\nfloat d = q.x - min(q.w, q.y);\nfloat e = 1.0e-10;\nreturn vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);\n}\nvec3 hsv2rgb(vec3 c) {\nvec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);\nvec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);\nreturn c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);\n}\nvoid main() {\n    vec3 hsv = rgb2hsv(texture2D(sTexture, vTextureCoord).rgb);\n    if ( ((hsv.g >= uParams[2]) && (hsv.g <= uParams[3]))\n        && ((hsv.b >= uParams[4]) && (hsv.b <= uParams[5]))\n        && ((hsv.r <= uParams[0]) || (hsv.r >= uParams[1])) ) {\n        hsv = hsv * vec3(uParams[6], uParams[7], uParams[8]);\n    } else {\n        hsv = hsv * vec3(uParams[9], uParams[10], uParams[11]);\n    }\n    gl_FragColor = vec4(hsv2rgb(clamp(hsv, 0.0, 1.0)), 1.0);\n}\n";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  209 */   private static final String FRAGMENT_SHADER_EMPHASIZE_RED_YELLOW_OES = String.format("#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nuniform float uParams[18];\nvec3 rgb2hsv(vec3 c) {\nvec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);\nvec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));\nvec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));\nfloat d = q.x - min(q.w, q.y);\nfloat e = 1.0e-10;\nreturn vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);\n}\nvec3 hsv2rgb(vec3 c) {\nvec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);\nvec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);\nreturn c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);\n}\nvoid main() {\n    vec3 hsv = rgb2hsv(texture2D(sTexture, vTextureCoord).rgb);\n    if ( ((hsv.g >= uParams[2]) && (hsv.g <= uParams[3]))\n        && ((hsv.b >= uParams[4]) && (hsv.b <= uParams[5]))\n        && ((hsv.r <= uParams[0]) || (hsv.r >= uParams[1])) ) {\n        hsv = hsv * vec3(uParams[6], uParams[7], uParams[8]);\n    } else {\n        hsv = hsv * vec3(uParams[9], uParams[10], uParams[11]);\n    }\n    gl_FragColor = vec4(hsv2rgb(clamp(hsv, 0.0, 1.0)), 1.0);\n}\n", new Object[] { "#extension GL_OES_EGL_image_external : require\n", "samplerExternalOES" });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String FRAGMENT_SHADER_EMPHASIZE_RED_YELLOW_WHITE_BASE = "#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nuniform float uParams[18];\nvec3 rgb2hsv(vec3 c) {\nvec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);\nvec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));\nvec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));\nfloat d = q.x - min(q.w, q.y);\nfloat e = 1.0e-10;\nreturn vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);\n}\nvec3 hsv2rgb(vec3 c) {\nvec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);\nvec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);\nreturn c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);\n}\nvoid main() {\n    vec3 hsv = rgb2hsv(texture2D(sTexture, vTextureCoord).rgb);\n    if ( ((hsv.g >= uParams[2]) && (hsv.g <= uParams[3]))\n        && ((hsv.b >= uParams[4]) && (hsv.b <= uParams[5]))\n        && ((hsv.r <= uParams[0]) || (hsv.r >= uParams[1])) ) {\n        hsv = hsv * vec3(uParams[6], uParams[7], uParams[8]);\n    } else if ((hsv.g < uParams[12]) && (hsv.b < uParams[13])) {\n        hsv = hsv * vec3(1.0, 0.0, 2.0);\n    } else {\n        hsv = hsv * vec3(uParams[9], uParams[10], uParams[11]);\n    }\n    gl_FragColor = vec4(hsv2rgb(clamp(hsv, 0.0, 1.0)), 1.0);\n}\n";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  237 */   private static final String FRAGMENT_SHADER_EMPHASIZE_RED_YELLOW_WHITE_OES = String.format("#version 100\n%sprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform %s sTexture;\nuniform float uParams[18];\nvec3 rgb2hsv(vec3 c) {\nvec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);\nvec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));\nvec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));\nfloat d = q.x - min(q.w, q.y);\nfloat e = 1.0e-10;\nreturn vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);\n}\nvec3 hsv2rgb(vec3 c) {\nvec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);\nvec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);\nreturn c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);\n}\nvoid main() {\n    vec3 hsv = rgb2hsv(texture2D(sTexture, vTextureCoord).rgb);\n    if ( ((hsv.g >= uParams[2]) && (hsv.g <= uParams[3]))\n        && ((hsv.b >= uParams[4]) && (hsv.b <= uParams[5]))\n        && ((hsv.r <= uParams[0]) || (hsv.r >= uParams[1])) ) {\n        hsv = hsv * vec3(uParams[6], uParams[7], uParams[8]);\n    } else if ((hsv.g < uParams[12]) && (hsv.b < uParams[13])) {\n        hsv = hsv * vec3(1.0, 0.0, 2.0);\n    } else {\n        hsv = hsv * vec3(uParams[9], uParams[10], uParams[11]);\n    }\n    gl_FragColor = vec4(hsv2rgb(clamp(hsv, 0.0, 1.0)), 1.0);\n}\n", new Object[] { "#extension GL_OES_EGL_image_external : require\n", "samplerExternalOES" });
/*      */   
/*  239 */   private final Object mSync = new Object();
/*      */ 
/*      */   
/*      */   private final RenderHolderCallback mCallback;
/*      */ 
/*      */   
/*      */   private volatile boolean isRunning;
/*      */ 
/*      */   
/*      */   private File mCaptureFile;
/*      */ 
/*      */   
/*      */   private final RendererTask mRendererTask;
/*      */   
/*      */   private static final int REQUEST_DRAW = 1;
/*      */   
/*      */   private static final int REQUEST_UPDATE_SIZE = 2;
/*      */   
/*      */   private static final int REQUEST_ADD_SURFACE = 3;
/*      */   
/*      */   private static final int REQUEST_REMOVE_SURFACE = 4;
/*      */   
/*      */   private static final int REQUEST_RECREATE_MASTER_SURFACE = 5;
/*      */   
/*      */   private static final int REQUEST_MIRROR = 6;
/*      */   
/*      */   private static final int REQUEST_CHANGE_EFFECT = 7;
/*      */   
/*      */   private static final int REQUEST_SET_PARAMS = 8;
/*      */   
/*      */   private final Runnable mCaptureTask;
/*      */ 
/*      */   
/*  272 */   public boolean isRunning() { return this.isRunning; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void release() {
/*  278 */     this.mRendererTask.release();
/*  279 */     synchronized (this.mSync) {
/*  280 */       this.isRunning = false;
/*  281 */       this.mSync.notifyAll();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  288 */   public Surface getSurface() { return this.mRendererTask.getSurface(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  293 */   public SurfaceTexture getSurfaceTexture() { return this.mRendererTask.getSurfaceTexture(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  298 */   public void reset() { this.mRendererTask.checkMasterSurface(); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  303 */   public void resize(int width, int height) { this.mRendererTask.resize(width, height); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  308 */   public void setMirror(int mirror) { this.mRendererTask.mirror(mirror % 4); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  314 */   public int getMirror() { return this.mRendererTask.mirror(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  320 */   public void addSurface(int id, Object surface, boolean isRecordable) { this.mRendererTask.addSurface(id, surface); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  326 */   public void addSurface(int id, Object surface, boolean isRecordable, int maxFps) { this.mRendererTask.addSurface(id, surface, maxFps); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  332 */   public void removeSurface(int id) { this.mRendererTask.removeSurface(id); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  337 */   public boolean isEnabled(int id) { return this.mRendererTask.isEnabled(id); }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  342 */   public void setEnabled(int id, boolean enable) { this.mRendererTask.setEnabled(id, enable); }
/*      */ 
/*      */ 
/*      */   
/*      */   public void requestFrame() {
/*  347 */     this.mRendererTask.removeRequest(1);
/*  348 */     this.mRendererTask.offer(1);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  353 */   public int getCount() { return this.mRendererTask.getCount(); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void captureStillAsync(String path) {
/*  364 */     File file = new File(path);
/*  365 */     synchronized (this.mSync) {
/*  366 */       this.mCaptureFile = file;
/*  367 */       this.mSync.notifyAll();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void captureStill(String path) {
/*  379 */     File file = new File(path);
/*  380 */     synchronized (this.mSync) {
/*  381 */       this.mCaptureFile = file;
/*  382 */       this.mSync.notifyAll();
/*      */       
/*      */       try {
/*  385 */         this.mSync.wait();
/*  386 */       } catch (InterruptedException interruptedException) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  397 */   public void changeEffect(int effect) { this.mRendererTask.changeEffect(effect % 11); }
/*      */ 
/*      */ 
/*      */   
/*  401 */   public int getCurrentEffect() { return this.mRendererTask.mEffect; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  410 */   public void setParams(float[] params) { this.mRendererTask.setParams(-1, params); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setParams(int effect, float[] params) throws IllegalArgumentException {
/*  421 */     if (effect > 0 && effect < 11) {
/*  422 */       this.mRendererTask.setParams(effect, params);
/*      */     } else {
/*  424 */       throw new IllegalArgumentException("invalid effect number:" + effect);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class RendererTask
/*      */     extends EglTask
/*      */   {
/*  446 */     private final Object mClientSync = new Object();
/*  447 */     private final SparseArray<RendererSurfaceRec> mClients = new SparseArray();
/*      */     private final EffectRendererHolder mParent;
/*  449 */     private final SparseArray<float[]> mParams = new SparseArray();
/*      */     private int muParamsLoc;
/*      */     private float[] mCurrentParams;
/*      */     private GLDrawer2D mDrawer;
/*      */     private int mTexId;
/*      */     private SurfaceTexture mMasterTexture;
/*  455 */     final float[] mTexMatrix = new float[16]; private Surface mMasterSurface;
/*      */     private int mVideoWidth;
/*      */     private int mVideoHeight;
/*  458 */     private int mMirror = 0;
/*      */     private int mEffect;
/*      */     private final SurfaceTexture.OnFrameAvailableListener mOnFrameAvailableListener;
/*      */     
/*      */     public RendererTask(EffectRendererHolder parent, int width, int height) {
/*  463 */       super(3, null, 2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1022 */       this.mOnFrameAvailableListener = new SurfaceTexture.OnFrameAvailableListener()
/*      */         {
/*      */           public void onFrameAvailable(SurfaceTexture surfaceTexture) {
/* 1025 */             RendererTask.this.offer(1);
/*      */           }
/*      */         }; this.mParent = parent; this.mVideoWidth = width; this.mVideoHeight = height;
/*      */     }
/*      */     @SuppressLint({"NewApi"}) protected void onStart() { this.mDrawer = new GLDrawer2D(true); handleReCreateMasterSurface(); this.mParams.clear(); this.mParams.put(9, new float[] { 0.17F, 0.85F, 0.5F, 1.0F, 0.4F, 1.0F, 1.0F, 1.0F, 5.0F, 1.0F, 1.0F, 1.0F }); this.mEffect = 0; handleChangeEffect(0); synchronized (this.mParent.mSync) { this.mParent.isRunning = true; this.mParent.mSync.notifyAll(); }  }
/*      */     protected void onStop() { synchronized (this.mParent.mSync) { this.mParent.isRunning = false; this.mParent.mSync.notifyAll(); }  makeCurrent(); if (this.mDrawer != null) { this.mDrawer.release(); this.mDrawer = null; }  handleReleaseMasterSurface(); handleRemoveAll(); }
/*      */     protected boolean onError(Exception e) { return false; }
/*      */     protected Object processRequest(int request, int arg1, int arg2, Object obj) { switch (request) { case 1: handleDraw(); break;case 2: handleResize(arg1, arg2); break;case 3: handleAddSurface(arg1, obj, arg2); break;case 4: handleRemoveSurface(arg1); break;case 5: handleReCreateMasterSurface(); break;case 7: handleChangeEffect(arg1); break;case 6: handleMirror(arg1); break;case 8: handleSetParam(arg1, (float[])obj); break; }  return null; }
/*      */     public Surface getSurface() { checkMasterSurface(); return this.mMasterSurface; } public SurfaceTexture getSurfaceTexture() { checkMasterSurface(); return this.mMasterTexture; } public void addSurface(int id, Object surface) { addSurface(id, surface, -1); } public void addSurface(int id, Object surface, int maxFps) { checkFinished(); if (!(surface instanceof SurfaceTexture) && !(surface instanceof Surface) && !(surface instanceof android.view.SurfaceHolder)) throw new IllegalArgumentException("Surface should be one of Surface, SurfaceTexture or SurfaceHolder");  synchronized (this.mClientSync) { if (this.mClients.get(id) == null) while (isRunning()) { if (offer(3, id, maxFps, surface)) try { this.mClientSync.wait(); break; } catch (InterruptedException interruptedException) { break; }   try { this.mClientSync.wait(10L); } catch (InterruptedException e) { break; }  }   }  } public void removeSurface(int id) { synchronized (this.mClientSync) { if (this.mClients.get(id) != null) while (isRunning()) { if (offer(4, id)) try { this.mClientSync.wait(); break; } catch (InterruptedException interruptedException) { break; }   try { this.mClientSync.wait(10L); } catch (InterruptedException e) { break; }  }   }  } public boolean isEnabled(int id) { synchronized (this.mClientSync) { RendererSurfaceRec rec = (RendererSurfaceRec)this.mClients.get(id); return (rec != null && rec.isEnabled()); }  } public void setEnabled(int id, boolean enable) { synchronized (this.mClientSync) { RendererSurfaceRec rec = (RendererSurfaceRec)this.mClients.get(id); if (rec != null) rec.setEnabled(enable);  }  } public void changeEffect(int effect) { checkFinished(); if (this.mEffect != effect) offer(7, effect);  } public void setParams(int effect, float[] params) { checkFinished(); offer(8, effect, 0, params); } public int getCount() { synchronized (this.mClientSync) { return this.mClients.size(); }  } public void resize(int width, int height) { checkFinished(); if (this.mVideoWidth != width || this.mVideoHeight != height) offer(2, width, height);  } public void mirror(int mirror) { checkFinished(); if (this.mMirror != mirror) offer(6, mirror);  } public int mirror() { return this.mMirror; } public void checkMasterSurface() { checkFinished(); if (this.mMasterSurface == null || !this.mMasterSurface.isValid()) { Log.d(TAG, "checkMasterSurface:invalid master surface"); offerAndWait(5, 0, 0, null); }  } private void checkFinished() { if (isFinished()) throw new RuntimeException("already finished");  } private void handleDraw() { if (this.mMasterSurface == null || !this.mMasterSurface.isValid()) { Log.e(TAG, "checkMasterSurface:invalid master surface"); offer(5); return; }  try { makeCurrent(); this.mMasterTexture.updateTexImage(); this.mMasterTexture.getTransformMatrix(this.mTexMatrix); } catch (Exception e) { Log.e(TAG, "draw:thread id =" + Thread.currentThread().getId(), e); offer(5); return; }  synchronized (this.mParent.mCaptureTask) { this.mParent.mCaptureTask.notify(); }  synchronized (this.mClientSync) { int n = this.mClients.size(); for (int i = n - 1; i >= 0; i--) { RendererSurfaceRec client = (RendererSurfaceRec)this.mClients.valueAt(i); if (client != null && client.canDraw()) try { client.draw(this.mDrawer, this.mTexId, this.mTexMatrix); } catch (Exception e) { Log.w(TAG, e); this.mClients.removeAt(i); client.release(); }   }  }  if (this.mParent.mCallback != null) try { this.mParent.mCallback.onFrameAvailable(); } catch (Exception e) { Log.w(TAG, e); }   GLES20.glClear(16384); GLES20.glFlush(); } private void handleAddSurface(int id, Object surface, int maxFps) { checkSurface(); synchronized (this.mClientSync) { RendererSurfaceRec client = (RendererSurfaceRec)this.mClients.get(id); if (client == null) { try { client = RendererSurfaceRec.newInstance(getEgl(), surface, maxFps); setMirror(client, this.mMirror); this.mClients.append(id, client); } catch (Exception e) { Log.w(TAG, "invalid surface: surface=" + surface, e); }  } else { Log.w(TAG, "surface is already added: id=" + id); }  this.mClientSync.notifyAll(); }  } private void handleRemoveSurface(int id) { synchronized (this.mClientSync) { RendererSurfaceRec client = (RendererSurfaceRec)this.mClients.get(id); if (client != null) { this.mClients.remove(id); client.release(); }  checkSurface(); this.mClientSync.notifyAll(); }  } private void handleRemoveAll() { synchronized (this.mClientSync) { int n = this.mClients.size(); for (int i = 0; i < n; i++) { RendererSurfaceRec client = (RendererSurfaceRec)this.mClients.valueAt(i); if (client != null) { makeCurrent(); client.release(); }  }  this.mClients.clear(); }  } private void checkSurface() { synchronized (this.mClientSync) { int n = this.mClients.size(); for (int i = 0; i < n; i++) { RendererSurfaceRec client = (RendererSurfaceRec)this.mClients.valueAt(i); if (client != null && !client.isValid()) { int id = this.mClients.keyAt(i); ((RendererSurfaceRec)this.mClients.valueAt(i)).release(); this.mClients.remove(id); }  }  }  } @SuppressLint({"NewApi"}) private void handleReCreateMasterSurface() { makeCurrent(); handleReleaseMasterSurface(); makeCurrent(); this.mTexId = GLHelper.initTex(36197, 9728); this.mMasterTexture = new SurfaceTexture(this.mTexId); this.mMasterSurface = new Surface(this.mMasterTexture); if (BuildCheck.isAndroid4_1()) this.mMasterTexture.setDefaultBufferSize(this.mVideoWidth, this.mVideoHeight);  this.mMasterTexture.setOnFrameAvailableListener(this.mOnFrameAvailableListener); try { if (this.mParent.mCallback != null) this.mParent.mCallback.onCreate(this.mMasterSurface);  } catch (Exception e) { Log.w(TAG, e); }  } private void handleReleaseMasterSurface() { try { if (this.mParent.mCallback != null) this.mParent.mCallback.onDestroy();  } catch (Exception e) { Log.w(TAG, e); }  this.mMasterSurface = null; if (this.mMasterTexture != null) { this.mMasterTexture.release(); this.mMasterTexture = null; }  if (this.mTexId != 0) { GLHelper.deleteTex(this.mTexId); this.mTexId = 0; }  } @SuppressLint({"NewApi"}) private void handleResize(int width, int height) { this.mVideoWidth = width; this.mVideoHeight = height; if (BuildCheck.isAndroid4_1()) this.mMasterTexture.setDefaultBufferSize(this.mVideoWidth, this.mVideoHeight);  } private void handleChangeEffect(int effect) { this.mEffect = effect; switch (effect) { case 0: this.mDrawer.updateShader("#version 100\n#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nuniform samplerExternalOES sTexture;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n  gl_FragColor = texture2D(sTexture, vTextureCoord);\n}"); break;case 1: this.mDrawer.updateShader(FRAGMENT_SHADER_GRAY_OES); break;case 2: this.mDrawer.updateShader(FRAGMENT_SHADER_GRAY_REVERSE_OES); break;case 3: this.mDrawer.updateShader(FRAGMENT_SHADER_BIN_OES); break;case 4: this.mDrawer.updateShader(FRAGMENT_SHADER_BIN_YELLOW_OES); break;case 5: this.mDrawer.updateShader(FRAGMENT_SHADER_BIN_GREEN_OES); break;case 6: this.mDrawer.updateShader(FRAGMENT_SHADER_BIN_REVERSE_OES); break;case 7: this.mDrawer.updateShader(FRAGMENT_SHADER_BIN_REVERSE_YELLOW_OES); break;case 8: this.mDrawer.updateShader(FRAGMENT_SHADER_BIN_REVERSE_GREEN_OES); break;case 9: this.mDrawer.updateShader(FRAGMENT_SHADER_EMPHASIZE_RED_YELLOW_OES); break;case 10: this.mDrawer.updateShader(FRAGMENT_SHADER_EMPHASIZE_RED_YELLOW_WHITE_OES); break; }  this.muParamsLoc = this.mDrawer.glGetUniformLocation("uParams"); this.mCurrentParams = (float[])this.mParams.get(effect); updateParams(); } private void handleMirror(int mirror) { this.mMirror = mirror; synchronized (this.mClientSync) { int n = this.mClients.size(); for (int i = 0; i < n; i++) { RendererSurfaceRec client = (RendererSurfaceRec)this.mClients.valueAt(i); if (client != null) setMirror(client, mirror);  }  }  } private void setMirror(RendererSurfaceRec client, int mirror) { float[] mvp = client.mMvpMatrix; switch (mirror) { case 0: mvp[0] = Math.abs(mvp[0]); mvp[5] = Math.abs(mvp[5]); break;case 1: mvp[0] = -Math.abs(mvp[0]); mvp[5] = Math.abs(mvp[5]); break;case 2: mvp[0] = Math.abs(mvp[0]); mvp[5] = -Math.abs(mvp[5]); break;case 3: mvp[0] = -Math.abs(mvp[0]); mvp[5] = -Math.abs(mvp[5]); break; }  } private void handleSetParam(int effect, float[] params) { if (effect < 0 || this.mEffect == effect) { this.mCurrentParams = params; this.mParams.put(this.mEffect, params); updateParams(); } else { this.mParams.put(effect, params); }  } private void updateParams() { int n = Math.min((this.mCurrentParams != null) ? this.mCurrentParams.length : 0, 18); if (this.muParamsLoc >= 0 && n > 0) { this.mDrawer.glUseProgram(); GLES20.glUniform1fv(this.muParamsLoc, n, this.mCurrentParams, 0); }  }
/* 1034 */   } public EffectRendererHolder(int width, int height, @Nullable RenderHolderCallback callback) { this.mCaptureTask = new Runnable()
/*      */       {
/*      */         EGLBase egl;
/*      */         
/*      */         EGLBase.IEglSurface captureSurface;
/*      */         GLDrawer2D drawer;
/*      */         
/*      */         public void run() {
/* 1042 */           synchronized (EffectRendererHolder.this.mSync) {
/*      */             
/* 1044 */             if (!EffectRendererHolder.this.isRunning) {
/*      */               try {
/* 1046 */                 EffectRendererHolder.this.mSync.wait();
/* 1047 */               } catch (InterruptedException interruptedException) {}
/*      */             }
/*      */           } 
/*      */           
/* 1051 */           init();
/* 1052 */           if (this.egl.getGlVersion() > 2) {
/* 1053 */             captureLoopGLES3();
/*      */           } else {
/* 1055 */             captureLoopGLES2();
/*      */           } 
/*      */           
/* 1058 */           release();
/*      */         }
/*      */ 
/*      */         
/*      */         private final void init() {
/* 1063 */           this.egl = EGLBase.createFrom(3, EffectRendererHolder.this.mRendererTask.getContext(), false, 0, false);
/* 1064 */           this.captureSurface = this.egl.createOffscreen(EffectRendererHolder.this.mRendererTask.mVideoWidth, EffectRendererHolder.this.mRendererTask.mVideoHeight);
/* 1065 */           this.drawer = new GLDrawer2D(true);
/* 1066 */           this.drawer.getMvpMatrix()[5] = this.drawer.getMvpMatrix()[5] * -1.0F;
/*      */         }
/*      */         
/*      */         private final void captureLoopGLES2() {
/* 1070 */           int width = -1, height = -1;
/* 1071 */           ByteBuffer buf = null;
/* 1072 */           File captureFile = null;
/*      */           
/* 1074 */           while (EffectRendererHolder.this.isRunning) {
/* 1075 */             synchronized (EffectRendererHolder.this.mSync) {
/* 1076 */               if (captureFile == null) {
/* 1077 */                 if (EffectRendererHolder.this.mCaptureFile == null) {
/*      */                   try {
/* 1079 */                     EffectRendererHolder.this.mSync.wait();
/* 1080 */                   } catch (InterruptedException e) {
/*      */                     break;
/*      */                   } 
/*      */                 }
/* 1084 */                 if (EffectRendererHolder.this.mCaptureFile != null) {
/*      */                   
/* 1086 */                   captureFile = EffectRendererHolder.this.mCaptureFile;
/* 1087 */                   EffectRendererHolder.this.mCaptureFile = null;
/*      */                 } 
/*      */                 continue;
/*      */               } 
/* 1091 */               if ((((buf == null) ? 1 : 0) | ((width != EffectRendererHolder.this.mRendererTask.mVideoWidth) ? 1 : 0)) != 0 || height != EffectRendererHolder.this.mRendererTask.mVideoHeight) {
/*      */                 
/* 1093 */                 width = EffectRendererHolder.this.mRendererTask.mVideoWidth;
/* 1094 */                 height = EffectRendererHolder.this.mRendererTask.mVideoHeight;
/* 1095 */                 buf = ByteBuffer.allocateDirect(width * height * 4);
/* 1096 */                 buf.order(ByteOrder.LITTLE_ENDIAN);
/* 1097 */                 if (this.captureSurface != null) {
/* 1098 */                   this.captureSurface.release();
/* 1099 */                   this.captureSurface = null;
/*      */                 } 
/* 1101 */                 this.captureSurface = this.egl.createOffscreen(width, height);
/*      */               } 
/* 1103 */               if (EffectRendererHolder.this.isRunning) {
/* 1104 */                 this.captureSurface.makeCurrent();
/* 1105 */                 this.drawer.draw(EffectRendererHolder.this.mRendererTask.mTexId, EffectRendererHolder.this.mRendererTask.mTexMatrix, 0);
/* 1106 */                 this.captureSurface.swap();
/* 1107 */                 buf.clear();
/* 1108 */                 GLES20.glReadPixels(0, 0, width, height, 6408, 5121, buf);
/*      */                 
/* 1110 */                 Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.PNG;
/* 1111 */                 if (captureFile.toString().endsWith(".jpg")) {
/* 1112 */                   compressFormat = Bitmap.CompressFormat.JPEG;
/*      */                 }
/* 1114 */                 BufferedOutputStream os = null;
/*      */                 try {
/*      */                   try {
/* 1117 */                     os = new BufferedOutputStream(new FileOutputStream(captureFile));
/* 1118 */                     Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
/* 1119 */                     buf.clear();
/* 1120 */                     bmp.copyPixelsFromBuffer(buf);
/* 1121 */                     bmp.compress(compressFormat, 90, os);
/* 1122 */                     bmp.recycle();
/* 1123 */                     os.flush();
/*      */                   } finally {
/* 1125 */                     if (os != null) os.close(); 
/*      */                   } 
/* 1127 */                 } catch (FileNotFoundException e) {
/* 1128 */                   Log.w(TAG, "failed to save file", e);
/* 1129 */                 } catch (IOException e) {
/* 1130 */                   Log.w(TAG, "failed to save file", e);
/*      */                 } 
/*      */               } 
/*      */               
/* 1134 */               captureFile = null;
/* 1135 */               EffectRendererHolder.this.mSync.notifyAll();
/*      */             } 
/*      */           } 
/*      */         }
/*      */ 
/*      */         
/*      */         private final void captureLoopGLES3() {
/* 1142 */           int width = -1, height = -1;
/* 1143 */           ByteBuffer buf = null;
/* 1144 */           File captureFile = null;
/*      */           
/* 1146 */           while (EffectRendererHolder.this.isRunning) {
/* 1147 */             synchronized (EffectRendererHolder.this.mSync) {
/* 1148 */               if (captureFile == null) {
/* 1149 */                 if (EffectRendererHolder.this.mCaptureFile == null) {
/*      */                   try {
/* 1151 */                     EffectRendererHolder.this.mSync.wait();
/* 1152 */                   } catch (InterruptedException e) {
/*      */                     break;
/*      */                   } 
/*      */                 }
/* 1156 */                 if (EffectRendererHolder.this.mCaptureFile != null) {
/*      */                   
/* 1158 */                   captureFile = EffectRendererHolder.this.mCaptureFile;
/* 1159 */                   EffectRendererHolder.this.mCaptureFile = null;
/*      */                 } 
/*      */                 continue;
/*      */               } 
/* 1163 */               if ((((buf == null) ? 1 : 0) | ((width != EffectRendererHolder.this.mRendererTask.mVideoWidth) ? 1 : 0)) != 0 || height != EffectRendererHolder.this.mRendererTask.mVideoHeight) {
/* 1164 */                 width = EffectRendererHolder.this.mRendererTask.mVideoWidth;
/* 1165 */                 height = EffectRendererHolder.this.mRendererTask.mVideoHeight;
/* 1166 */                 buf = ByteBuffer.allocateDirect(width * height * 4);
/* 1167 */                 buf.order(ByteOrder.LITTLE_ENDIAN);
/* 1168 */                 if (this.captureSurface != null) {
/* 1169 */                   this.captureSurface.release();
/* 1170 */                   this.captureSurface = null;
/*      */                 } 
/* 1172 */                 this.captureSurface = this.egl.createOffscreen(width, height);
/*      */               } 
/* 1174 */               if (EffectRendererHolder.this.isRunning) {
/* 1175 */                 this.captureSurface.makeCurrent();
/* 1176 */                 this.drawer.draw(EffectRendererHolder.this.mRendererTask.mTexId, EffectRendererHolder.this.mRendererTask.mTexMatrix, 0);
/* 1177 */                 this.captureSurface.swap();
/* 1178 */                 buf.clear();
/* 1179 */                 GLES20.glReadPixels(0, 0, width, height, 6408, 5121, buf);
/*      */                 
/* 1181 */                 Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.PNG;
/* 1182 */                 if (captureFile.toString().endsWith(".jpg")) {
/* 1183 */                   compressFormat = Bitmap.CompressFormat.JPEG;
/*      */                 }
/* 1185 */                 BufferedOutputStream os = null;
/*      */                 try {
/*      */                   try {
/* 1188 */                     os = new BufferedOutputStream(new FileOutputStream(captureFile));
/* 1189 */                     Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
/* 1190 */                     buf.clear();
/* 1191 */                     bmp.copyPixelsFromBuffer(buf);
/* 1192 */                     bmp.compress(compressFormat, 90, os);
/* 1193 */                     bmp.recycle();
/* 1194 */                     os.flush();
/*      */                   } finally {
/* 1196 */                     if (os != null) os.close(); 
/*      */                   } 
/* 1198 */                 } catch (FileNotFoundException e) {
/* 1199 */                   Log.w(TAG, "failed to save file", e);
/* 1200 */                 } catch (IOException e) {
/* 1201 */                   Log.w(TAG, "failed to save file", e);
/*      */                 } 
/*      */               } 
/*      */               
/* 1205 */               captureFile = null;
/* 1206 */               EffectRendererHolder.this.mSync.notifyAll();
/*      */             } 
/*      */           } 
/*      */         }
/*      */         
/*      */         private final void release() {
/* 1212 */           if (this.captureSurface != null) {
/* 1213 */             this.captureSurface.makeCurrent();
/* 1214 */             if (this.drawer != null) {
/* 1215 */               this.drawer.release();
/*      */             }
/* 1217 */             this.captureSurface.release();
/* 1218 */             this.captureSurface = null;
/*      */           } 
/* 1220 */           if (this.drawer != null) {
/* 1221 */             this.drawer.release();
/* 1222 */             this.drawer = null;
/*      */           } 
/* 1224 */           if (this.egl != null) {
/* 1225 */             this.egl.release();
/* 1226 */             this.egl = null;
/*      */           } 
/*      */         }
/*      */       };
/*      */     this.mCallback = callback;
/*      */     this.mRendererTask = new RendererTask(this, width, height);
/*      */     (new Thread((Runnable)this.mRendererTask, TAG)).start();
/*      */     if (!this.mRendererTask.waitReady())
/*      */       throw new RuntimeException("failed to start renderer thread"); 
/*      */     (new Thread(this.mCaptureTask, "CaptureTask")).start();
/*      */     synchronized (this.mSync) {
/*      */       if (!this.isRunning)
/*      */         try {
/*      */           this.mSync.wait();
/*      */         } catch (InterruptedException interruptedException) {} 
/*      */     }  }
/*      */ 
/*      */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\glutils\EffectRendererHolder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */