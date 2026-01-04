<template>
  <div class="user-list-container">
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
              <option value="男">男</option>
              <option value="女">女</option>
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
        
        <div class="search-actions">
          <div class="radio-group">
            <label><input type="radio" v-model="searchForm.searchType" value="AND" /> AND検索</label>
            <label><input type="radio" v-model="searchForm.searchType" value="OR" /> OR検索</label>
          </div>
          <button @click="handleSearch" class="search-btn">検索</button>
          <button @click="handleReset" class="reset-btn">リセット</button>
        </div>
      </div>
    </div>
    
    <div class="user-table-section">
      <h2>検索結果 ({{ users.length }}件)</h2>
      <div v-if="users.length === 0" class="no-data">データがありません</div>
      <table v-else class="user-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>名前</th>
            <th>性別</th>
            <th>年齢</th>
            <th>郵便番号</th>
            <th>住所</th>
            <th>電話番号</th>
            <th>国籍</th>
            <th>好きな食べ物</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id" @click="goToDetail(user.id)" class="clickable">
            <td>{{ user.id }}</td>
            <td>{{ user.name }}</td>
            <td>{{ user.gender }}</td>
            <td>{{ user.age }}</td>
            <td>{{ user.postalCode }}</td>
            <td>{{ user.prefecture }} {{ user.city }} {{ user.address }}</td>
            <td>{{ user.phoneNumber }}</td>
            <td>{{ user.nationality }}</td>
            <td>{{ user.favoriteFoods.join(', ') }}</td>
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
    this.loadAllUsers()
  },
  methods: {
    async loadAllUsers() {
      try {
        const response = await fetch('http://localhost:8081/api/users/list')
        this.users = await response.json()
      } catch (error) {
        console.error('ユーザー取得エラー:', error)
      }
    },
    async handleSearch() {
      const params = new URLSearchParams()
      
      if (this.searchForm.name) params.append('name', this.searchForm.name)
      if (this.searchForm.gender) params.append('gender', this.searchForm.gender)
      if (this.searchForm.age) params.append('age', this.searchForm.age)
      if (this.searchForm.food) params.append('food', this.searchForm.food)
      params.append('searchType', this.searchForm.searchType)
      
      try {
        const response = await fetch(`http://localhost:8081/api/users/search?${params.toString()}`)
        this.users = await response.json()
      } catch (error) {
        console.error('検索エラー:', error)
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
    goToDetail(userId) {
      this.$router.push(`/users/${userId}`)
    }
  }
}
</script>

<style scoped>
.user-list-container {
  max-width: 1200px;
  margin: 30px auto;
  padding: 20px;
}

h1, h2 {
  margin-bottom: 20px;
}

.search-section {
  background: #f5f5f5;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 30px;
}

.form-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px;
  margin-bottom: 15px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-group label {
  margin-bottom: 5px;
  font-weight: bold;
  font-size: 14px;
}

.form-group input,
.form-group select {
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.search-actions {
  display: flex;
  align-items: center;
  gap: 15px;
}

.radio-group {
  display: flex;
  gap: 20px;
}

.radio-group label {
  display: flex;
  align-items: center;
}

.radio-group input {
  margin-right: 5px;
}

.search-btn, .reset-btn {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.search-btn {
  background-color: #4CAF50;
  color: white;
}

.reset-btn {
  background-color: #999;
  color: white;
}

.user-table-section {
  margin-top: 30px;
}

.no-data {
  text-align: center;
  padding: 40px;
  color: #999;
}

.user-table {
  width: 100%;
  border-collapse: collapse;
  background: white;
}

.user-table th,
.user-table td {
  padding: 12px;
  text-align: left;
  border: 1px solid #ddd;
}

.user-table th {
  background-color: #f0f0f0;
  font-weight: bold;
}

.user-table tbody tr.clickable {
  cursor: pointer;
  transition: background-color 0.2s;
}

.user-table tbody tr.clickable:hover {
  background-color: #f9f9f9;
}
</style>