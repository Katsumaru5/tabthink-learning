<template>
  <div class="user-list-container">
    <div class="header">
      <div class="welcome">こんにちは {{ currentUsername }}さん</div>
      <button @click="handleLogout" class="logout-btn">ログアウト</button>
    </div>
    
    <h1>ユーザー一覧</h1>
    
    <div class="search-section">
      <h2>検索条件</h2>
      <div class="search-form">
        <div class="form-row">
          <div class="form-group">
            <label>名前</label>
            <input v-model="searchForm.name" type="text" placeholder="部分一致" />
          </div>
          
		<div class="form-group">
			<label>性別</label>
			<select v-model="searchForm.gender">
				<option value="">すべて</option>
				<option value="男性">男性</option>
				<option value="女性">女性</option>
				<option value="その他">その他</option>
			</select>
		</div>
          
          <div class="form-group">
            <label>年齢</label>
            <input v-model.number="searchForm.age" type="number" placeholder="完全一致" />
          </div>
          
          <div class="form-group">
            <label>好きな食べ物</label>
            <input v-model="searchForm.food" type="text" placeholder="部分一致" />
          </div>
        </div>
        
        <div class="form-row">
          <div class="form-group">
            <label>検索タイプ</label>
            <div class="radio-group">
              <label>
                <input type="radio" v-model="searchForm.searchType" value="AND" />
                AND検索
              </label>
              <label>
                <input type="radio" v-model="searchForm.searchType" value="OR" />
                OR検索
              </label>
            </div>
          </div>
        </div>
        
        <div class="button-group">
          <button @click="handleSearch" class="search-btn">検索</button>
          <button @click="handleReset" class="reset-btn">リセット</button>
        </div>
      </div>
    </div>
    
    <div class="result-section">
      <h2>検索結果 ({{ users.length }}件)</h2>
      
      <div v-if="users.length === 0" class="no-data">
        データがありません
      </div>
      
      <table v-else class="user-table">
        <thead>
          <tr>
            <th>No.</th>
            <th>名前</th>
            <th>性別</th>
            <th>年齢</th>
            <th>郵便番号</th>
            <th>住所</th>
            <th>電話番号</th>
            <th>国籍</th>
            <th>好きな食べ物</th>
            <th>メニュー</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(user, index) in users" :key="user.id">
            <td>{{ index + 1 }}</td>
            <td>{{ user.name }}</td>
            <td>{{ user.gender }}</td>
            <td>{{ user.age }}</td>
            <td>{{ user.postalCode }}</td>
            <td>{{ user.prefecture }}{{ user.city }}{{ user.address }}</td>
            <td>{{ user.phoneNumber }}</td>
            <td>{{ user.nationality }}</td>
            <td>
              <template v-if="user.favoriteFoods && user.favoriteFoods.length > 0">
                {{ user.favoriteFoods.join(', ') }}
              </template>
              <template v-else>
                なし
              </template>
            </td>
            <td>
              <button @click="goToEdit(user.id)" class="edit-btn">
                編集
              </button>
              <button @click="handleDelete(user.id)" class="delete-btn">
                削除
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
export default {
  name: 'UserListView',
  data() {
    return {
      currentUsername: '',
      users: [],
      searchForm: {
        name: '',
        gender: '',
        age: null,
        food: '',
        searchType: 'AND'
      }
    }
  },
  created() {
    this.currentUsername = localStorage.getItem('username') || 'ゲスト'
    this.loadAllUsers()
  },
  methods: {
    async loadAllUsers() {
      try {
        const response = await fetch('http://localhost:8081/api/users/list')
        const data = await response.json()
        console.log('取得したユーザーデータ:', data)
        console.log('最初のユーザーのfavoriteFoods:', data[0]?.favoriteFoods)
        this.users = data
      } catch (error) {
        console.error('ユーザー一覧の取得に失敗しました', error)
      }
    },
    async handleSearch() {
      const params = new URLSearchParams()
      
      if (this.searchForm.name) {
        params.append('name', this.searchForm.name)
      }
      if (this.searchForm.gender) {
        params.append('gender', this.searchForm.gender)
      }
      if (this.searchForm.age) {
        params.append('age', this.searchForm.age)
      }
      if (this.searchForm.food) {
        params.append('food', this.searchForm.food)
      }
      params.append('searchType', this.searchForm.searchType)
      
      try {
        const response = await fetch(
          `http://localhost:8081/api/users/search?${params.toString()}`
        )
        const data = await response.json()
        this.users = data
      } catch (error) {
        console.error('検索に失敗しました', error)
      }
    },
    handleReset() {
      this.searchForm = {
        name: '',
        gender: '',
        age: null,
        food: '',
        searchType: 'AND'
      }
      this.loadAllUsers()
    },
    handleLogout() {
      localStorage.removeItem('username')
      this.$router.push('/login')
    },
    goToEdit(userId) {
      this.$router.push(`/users/${userId}/edit`)
    },
    async handleDelete(userId) {
      if (!confirm('本当に削除しますか？')) {
        return
      }
      
      try {
        const response = await fetch(
          `http://localhost:8081/api/users/${userId}`,
          { method: 'DELETE' }
        )
        const data = await response.json()
        
        if (data.success) {
          alert('削除しました')
          this.loadAllUsers()
        } else {
          alert(data.error || '削除に失敗しました')
        }
      } catch (error) {
        console.error('削除に失敗しました', error)
        alert('削除に失敗しました')
      }
    }
  }
}
</script>

<style scoped>
.user-list-container {
  padding: 20px;
  max-width: 100%;
  margin: 0 auto;
  overflow-x: auto;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.welcome {
  font-size: 18px;
  font-weight: bold;
}

.logout-btn {
  padding: 8px 16px;
  background-color: #f44336;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.logout-btn:hover {
  background-color: #d32f2f;
}

h1 {
  margin-bottom: 20px;
}

.search-section {
  background-color: #f5f5f5;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

h2 {
  margin-bottom: 15px;
}

.search-form .form-row {
  display: flex;
  gap: 15px;
  margin-bottom: 15px;
  flex-wrap: wrap;
}

.form-group {
  display: flex;
  flex-direction: column;
  flex: 1;
  min-width: 150px;
}

.form-group label {
  margin-bottom: 5px;
  font-weight: bold;
}

.form-group input,
.form-group select {
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.radio-group {
  display: flex;
  gap: 15px;
}

.radio-group label {
  display: flex;
  align-items: center;
  gap: 5px;
  font-weight: normal;
}

.button-group {
  display: flex;
  gap: 10px;
}

.search-btn,
.reset-btn {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
}

.search-btn {
  background-color: #4CAF50;
  color: white;
}

.search-btn:hover {
  background-color: #45a049;
}

.reset-btn {
  background-color: #9E9E9E;
  color: white;
}

.reset-btn:hover {
  background-color: #757575;
}

.result-section {
  margin-top: 20px;
}

.no-data {
  text-align: center;
  padding: 40px;
  color: #999;
}

.user-table {
  width: 100%;
  border-collapse: collapse;
  background-color: white;
  table-layout: auto;
}

.user-table th,
.user-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ddd;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.user-table th {
  background-color: #f5f5f5;
  font-weight: bold;
}

.user-table tr:hover {
  background-color: #f9f9f9;
}

.user-table td:nth-child(9) {
  white-space: normal;
  min-width: 150px;
  max-width: 250px;
}

.edit-btn,
.delete-btn {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 5px;
}

.edit-btn {
  background-color: #2196F3;
  color: white;
}

.edit-btn:hover {
  background-color: #0b7dda;
}

.delete-btn {
  background-color: #f44336;
  color: white;
}

.delete-btn:hover {
  background-color: #d32f2f;
}
</style>