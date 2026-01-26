<template>
  <div class="login-container">
    <h1>ログイン</h1>
    <form @submit.prevent="handleLogin">
      <div class="form-group">
        <input 
          v-model="username" 
          type="text" 
          placeholder="ユーザー名"
          required
        />
      </div>
      <div class="form-group">
        <input 
          v-model="password" 
          type="password" 
          placeholder="パスワード"
          required
        />
      </div>
      <div v-if="errorMessage" class="error">{{ errorMessage }}</div>
      <button type="submit" class="login-btn">ログイン</button>
      <button type="button" class="register-btn" @click="goToRegister">
        初めての方はこちら
      </button>
    </form>
  </div>
</template>

<script>
export default {
  name: 'LoginView',
  data() {
    return {
      username: '',
      password: '',
      errorMessage: ''
    };
  },
  methods: {
    async handleLogin() {
		this.errorMessage = '';

	try {
		const response = await fetch('http://localhost:8081/api/users/login', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
			body: JSON.stringify({
				username: this.username,
				password: this.password
			})
		});

		const data = await response.json();
		// デバッグ用ログ
		console.log('=== ログインレスポンス ===');
		console.log('response.ok:', response.ok);
		console.log('data:', data);
		console.log('data.success:', data.success);
		console.log('data.user:', data.user);
		if (data.user) {
			console.log('data.user.name:', data.user.name);
		}
		console.log('=====================');
		
		if (response.ok && data.success) {
			// ユーザー名を保存
			if (data.user && data.user.name) {
				localStorage.setItem('userName', data.user.name);
			console.log('✅ localStorage.setItem("userName"):', data.user.name);
			
			// 保存確認
			const saved = localStorage.getItem('userName');
			console.log('✅ localStorage.getItem("userName"):', saved);
		} else {
			console.error('❌ data.user.name が存在しません');
		}
		this.$router.push('/users');
	} else {
		this.errorMessage = data.error || 'ログインに失敗しました';
	}
} catch (error) {
	console.error('ログインエラー:', error);
	this.errorMessage = '接続エラーが発生しました';
}
	}
  }
};
</script>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 50px auto;
  padding: 30px;
  border: 1px solid #ddd;
  border-radius: 8px;
}

h1 {
  text-align: center;
  margin-bottom: 30px;
}

.form-group {
  margin-bottom: 20px;
}

input {
  width: 100%;
  padding: 12px;
  font-size: 16px;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-sizing: border-box;
}

.login-btn, .register-btn {
  width: 100%;
  padding: 12px;
  font-size: 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-bottom: 10px;
}

.login-btn {
  background-color: #4CAF50;
  color: white;
}

.register-btn {
  background-color: #2196F3;
  color: white;
}

.error {
  color: red;
  margin-bottom: 15px;
  text-align: center;
}
</style>