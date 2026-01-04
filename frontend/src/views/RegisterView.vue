<template>
  <div class="register-container">
    <h1>ユーザー登録</h1>
    <form @submit.prevent="handleRegister">
      <div class="form-group">
        <label>ユーザー名</label>
        <input v-model="form.username" type="text" required />
      </div>
      
      <div class="form-group">
        <label>パスワード</label>
        <input v-model="form.password" type="password" required />
      </div>
      
      <div class="form-group">
        <label>氏名</label>
        <input v-model="form.name" type="text" required />
      </div>
      
      <div class="form-group">
        <label>年齢</label>
        <input v-model.number="form.age" type="number" min="0" max="150" />
      </div>
      
      <div class="form-group">
        <label>性別</label>
        <div class="radio-group">
          <label><input type="radio" v-model="form.gender" value="男" /> 男</label>
          <label><input type="radio" v-model="form.gender" value="女" /> 女</label>
          <label><input type="radio" v-model="form.gender" value="その他" /> その他</label>
        </div>
      </div>
      
      <div class="form-group">
        <label>郵便番号（ハイフンなし）</label>
        <input v-model="form.postalCode" type="text" @blur="fetchAddress" placeholder="1000001" />
      </div>
      
      <div class="form-group">
        <label>都道府県</label>
        <input v-model="form.prefecture" type="text" />
      </div>
      
      <div class="form-group">
        <label>市区町村</label>
        <input v-model="form.city" type="text" />
      </div>
      
      <div class="form-group">
        <label>市区町村以降</label>
        <input v-model="form.address" type="text" />
      </div>
      
      <div class="form-group">
        <label>電話番号</label>
        <input v-model="form.phoneNumber" type="tel" />
      </div>
      
      <div class="form-group">
        <label>国籍</label>
        <select v-model="form.nationality" required>
          <option value="">選択してください</option>
          <option v-for="(country, index) in countries" :key="index" :value="country">
            {{ country }}
          </option>
        </select>
      </div>
      
      <div class="form-group">
        <label>好きな食べ物（カンマ区切りで複数入力可）</label>
        <input v-model="favoriteFoodsInput" type="text" placeholder="例: ラーメン,寿司,カレー" />
      </div>
      
      <div v-if="errorMessage" class="error">{{ errorMessage }}</div>
      <div v-if="successMessage" class="success">{{ successMessage }}</div>
      
      <button type="submit" class="register-btn">登録</button>
      <button type="button" class="back-btn" @click="goToLogin">戻る</button>
    </form>
  </div>
</template>

<script>
import countries from 'i18n-iso-countries'
import ja from 'i18n-iso-countries/langs/ja.json'

countries.registerLocale(ja)

export default {
  name: 'RegisterView',
  data() {
    return {
      form: {
        username: '',
        password: '',
        name: '',
        age: null,
        gender: '',
        postalCode: '',
        prefecture: '',
        city: '',
        address: '',
        phoneNumber: '',
        nationality: ''
      },
      favoriteFoodsInput: '',
      errorMessage: '',
      successMessage: '',
      countries: []
    }
  },
  created() {
    const countryObj = countries.getNames('ja')
    this.countries = Object.values(countryObj).sort()
  },
  methods: {
    async fetchAddress() {
      if (this.form.postalCode.length === 7) {
        try {
          const response = await fetch(`https://zipcloud.ibsnet.co.jp/api/search?zipcode=${this.form.postalCode}`)
          const data = await response.json()
          
          if (data.results) {
            this.form.prefecture = data.results[0].address1
            this.form.city = data.results[0].address2 + data.results[0].address3
          }
        } catch (error) {
          console.error('住所取得エラー:', error)
        }
      }
    },
    async handleRegister() {
      this.errorMessage = ''
      this.successMessage = ''
      
      const favoriteFoods = this.favoriteFoodsInput
        .split(',')
        .map(food => food.trim())
        .filter(food => food !== '')
      
      try {
        const response = await fetch('http://localhost:8081/api/users/register', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            ...this.form,
            favoriteFoods: favoriteFoods
          })
        })
        
        const data = await response.json()
        
        if (response.ok) {
          this.successMessage = '登録成功！ログイン画面に移動します...'
          setTimeout(() => {
            this.$router.push('/login')
          }, 2000)
        } else {
          this.errorMessage = data.error
        }
      } catch (error) {
        this.errorMessage = '接続エラーが発生しました'
      }
    },
    goToLogin() {
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.register-container {
  max-width: 500px;
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

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

input, select {
  width: 100%;
  padding: 10px;
  font-size: 14px;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-sizing: border-box;
}

.radio-group {
  display: flex;
  gap: 20px;
}

.radio-group label {
  display: flex;
  align-items: center;
  font-weight: normal;
}

.radio-group input {
  width: auto;
  margin-right: 5px;
}

.register-btn, .back-btn {
  width: 100%;
  padding: 12px;
  font-size: 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-bottom: 10px;
}

.register-btn {
  background-color: #4CAF50;
  color: white;
}

.back-btn {
  background-color: #999;
  color: white;
}

.error {
  color: red;
  margin-bottom: 15px;
  text-align: center;
}

.success {
  color: green;
  margin-bottom: 15px;
  text-align: center;
}
</style>