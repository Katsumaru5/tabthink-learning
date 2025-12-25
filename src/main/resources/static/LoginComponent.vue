<template>
  <div class="login-container">
    <h2>ログイン</h2>

    <p v-if="showError" class="error">
      ログイン情報が間違っています
    </p>

    <form @submit.prevent="submitLogin">
      <div>
        ユーザ名：
        <input type="text" v-model="username" />
      </div>
      <div>
        パスワード：
        <input type="password" v-model="password" />
      </div>
      <button type="submit">ログイン</button>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const username = ref('')
const password = ref('')
const showError = ref(false)

// URLパラメータからエラー表示判定
onMounted(() => {
  const params = new URLSearchParams(window.location.search)
  if (params.get("error") === "true") {
    showError.value = true
  }
})

// フォーム送信処理
const submitLogin = () => {
  // ここでバックエンドに POST リクエストを送る想定
  // 例: fetch('/login', { method: 'POST', body: JSON.stringify({ username, password }) })
  console.log('送信:', username.value, password.value)
}
</script>

<style scoped>
.login-container {
  width: 300px;
  margin: 50px auto;
}

.error {
  color: red;
  margin-bottom: 10px;
}

input {
  width: 100%;
  margin-top: 4px;
  margin-bottom: 10px;
  padding: 4px;
  box-sizing: border-box;
}

button {
  width: 100%;
  padding: 6px;
}
</style>
