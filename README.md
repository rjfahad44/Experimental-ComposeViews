## ✨ Experimental Compose Views

A collection of experimental and animated Jetpack Compose views for modern Android UI development.

## 📦 Dependencies + 🎥 Demos

<table>
<tr>
<td align="center"> <b>🔹 SHIMMER</b><br>
<img src="https://github.com/rjfahad44/Experimental-ComposeViews/blob/dev_fahad/demo_media/shimmer_effect.gif?raw=true" width="200"><br><br>
<code id="code1"> </code> <button onclick="copyCode('code1')">Copy</button>
</td>
<td align="center"> <b>🐝 MOVEABLE_BEE</b><br>
<img src="https://github.com/rjfahad44/Experimental-ComposeViews/blob/dev_fahad/demo_media/moveable_bee.gif?raw=true" width="200"><br><br> 
<code id="code2">implementation("com.github.rjfahad44.Experimental-ComposeViews:moveableimage:v0.0.2-alpha")</code> 
<button onclick="copyCode('code2')">Copy</button>
</td>
<td align="center"> <b>💥 RANDOM_SHAKE</b><br>
<img src="https://github.com/rjfahad44/Experimental-ComposeViews/blob/dev_fahad/demo_media/random_shake.gif?raw=true" width="200"><br><br>
<code id="code3"> </code> <button onclick="copyCode('code3')">Copy</button>
</td>
</tr>
<tr> 
<td align="center"> <b>🌀 RANDOM_MOVEABLE</b><br>
<img src="https://github.com/rjfahad44/Experimental-ComposeViews/blob/dev_fahad/demo_media/random_moveable.gif?raw=true" width="200"><br><br>
<code id="code4"> </code> <button onclick="copyCode('code4')">Copy</button>
</td>
<td align="center"> <b>💡 DJ_PER_CHAR</b><br>
<img src="https://github.com/rjfahad44/Experimental-ComposeViews/blob/dev_fahad/demo_media/dj_lighting_per_character.gif?raw=true" width="200"><br><br>
<code id="code5"> </code> <button onclick="copyCode('code5')">Copy</button>
</td>
<td align="center"> <b>🌈 DJ_SHOW</b><br>
<img src="https://github.com/rjfahad44/Experimental-ComposeViews/blob/dev_fahad/demo_media/dj_show_light_effect.gif?raw=true" width="200"><br><br> 
<code id="code6"> </code> <button onclick="copyCode('code6')">Copy</button>
</td>
</tr>
<tr>
<td align="center"> <b>📄 SIMPLE_PAGER</b><br>
<img src="https://github.com/rjfahad44/Experimental-ComposeViews/blob/dev_fahad/demo_media/simple_pager.gif?raw=true" width="200"><br><br>
<code id="code7"> </code> <button onclick="copyCode('code7')">Copy</button>
</td> 
<td align="center"> <b>📚 STACK_PAGER</b><br>
<img src="https://github.com/rjfahad44/Experimental-ComposeViews/blob/dev_fahad/demo_media/fully_customizable_stack_pager.gif?raw=true" width="200"><br><br>
<code id="code8">implementation("com.github.rjfahad44.Experimental-ComposeViews:stackswipecardpager:v0.0.2-alpha")</code> 
<button onclick="copyCode('code8')">Copy</button>
</td>
<td></td>
</tr>
</table>

<script>
function copyCode(id) {
  const code = document.getElementById(id).innerText;
  navigator.clipboard.writeText(code).then(() => {
    alert('Code copied to clipboard!');
  }).catch(err => {
    alert('Failed to copy: ' + err);
  });
}
</script>


## 🚀 How to Use AND 🔧 Project Setup

First, make sure to add JitPack to your  **`settings.gradle.kts`**:

```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
```

