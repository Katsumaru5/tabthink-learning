<template>
  <div class="edit-container">
    <h2>ユーザー情報編集</h2>

    <div v-if="hasErrors" class="error-summary" ref="errorSummary">
      ⚠️ 入力内容に誤りがあります
    </div>

    <form @submit.prevent="handleUpdate" class="edit-form">
      <div class="form-group">
        <label>ユーザー名</label>
        <input type="text" v-model="formData.username" disabled />
      </div>

      <div class="form-group">
        <label>氏名 <span class="required">*</span></label>
        <input
          type="text"
          v-model="formData.name"
          :class="{ 'error-input': fieldErrors.name }"
          @input="clearFieldError('name')"
          placeholder="例: 山田太郎"
        />
        <div v-if="fieldErrors.name" class="error-message">
          {{ fieldErrors.name }}
        </div>
      </div>

      <div class="form-group">
        <label>性別 <span class="required">*</span></label>
        <div :class="{ 'error-input': fieldErrors.gender }">
          <label class="radio-label">
            <input
              type="radio"
              v-model="formData.gender"
              value="男性"
              @change="clearFieldError('gender')"
            />
            男性
          </label>
          <label class="radio-label">
            <input
              type="radio"
              v-model="formData.gender"
              value="女性"
              @change="clearFieldError('gender')"
            />
            女性
          </label>
          <label class="radio-label">
            <input
              type="radio"
              v-model="formData.gender"
              value="その他"
              @change="clearFieldError('gender')"
            />
            その他
          </label>
        </div>
        <div v-if="fieldErrors.gender" class="error-message">
          {{ fieldErrors.gender }}
        </div>
      </div>

      <div class="form-group">
        <label>年齢 <span class="required">*</span></label>
        <input
          type="number"
          v-model.number="formData.age"
          :class="{ 'error-input': fieldErrors.age }"
          @input="clearFieldError('age')"
          min="0"
          max="150"
          placeholder="例: 25"
        />
        <div v-if="fieldErrors.age" class="error-message">
          {{ fieldErrors.age }}
        </div>
      </div>

      <div class="form-group">
        <label>郵便番号 <span class="required">*</span></label>
        <input
          type="text"
          v-model="formData.postalCode"
          :class="{ 'error-input': fieldErrors.postalCode || postalCodeNotFound }"
          @input="formatPostalCodeNoHyphen"
          @blur="fetchAddress"
          maxlength="7"
          placeholder="例: 1000001（ハイフンなし7桁）"
        />
        <div v-if="fieldErrors.postalCode" class="error-message">
          {{ fieldErrors.postalCode }}
        </div>
        <div v-if="postalCodeNotFound" class="error-message">
          この郵便番号は存在しません
        </div>
      </div>

      <div class="form-group">
        <label>都道府県 <span class="required">*</span></label>
        <input
          type="text"
          v-model="formData.prefecture"
          :class="{ 'error-input': fieldErrors.prefecture }"
          @input="clearFieldError('prefecture')"
          placeholder="例: 東京都"
        />
        <div v-if="fieldErrors.prefecture" class="error-message">
          {{ fieldErrors.prefecture }}
        </div>
      </div>

      <div class="form-group">
        <label>市区町村 <span class="required">*</span></label>
        <input
          type="text"
          v-model="formData.city"
          :class="{ 'error-input': fieldErrors.city }"
          @input="clearFieldError('city')"
          placeholder="例: 千代田区"
        />
        <div v-if="fieldErrors.city" class="error-message">
          {{ fieldErrors.city }}
        </div>
      </div>

      <div class="form-group">
        <label>住所 <span class="required">*</span></label>
        <input
          type="text"
          v-model="formData.address"
          :class="{ 'error-input': fieldErrors.address }"
          @input="clearFieldError('address')"
          placeholder="例: 千代田1-1-1"
        />
        <div v-if="fieldErrors.address" class="error-message">
          {{ fieldErrors.address }}
        </div>
      </div>

      <div class="form-group">
        <label>電話番号 <span class="required">*</span></label>
        <input
          type="tel"
          v-model="formData.phoneNumber"
          :class="{ 'error-input': fieldErrors.phoneNumber }"
          @input="formatPhoneNumberNoHyphen"
          maxlength="11"
          placeholder="例: 09012345678（ハイフンなし10〜11桁）"
        />
        <div v-if="fieldErrors.phoneNumber" class="error-message">
          {{ fieldErrors.phoneNumber }}
        </div>
      </div>

      <div class="form-group">
        <label>国籍 <span class="required">*</span></label>
        <select
          v-model="formData.nationality"
          :class="{ 'error-input': fieldErrors.nationality }"
          @change="clearFieldError('nationality')"
        >
          <option value="">選択してください</option>
          <option v-for="country in countries" :key="country" :value="country">
            {{ country }}
          </option>
        </select>
        <div v-if="fieldErrors.nationality" class="error-message">
          {{ fieldErrors.nationality }}
        </div>
      </div>

      <div class="form-group">
        <label>好きな食べ物</label>
        <input
          type="text"
          v-model="favoriteFoodsInput"
          :class="{ 'error-input': fieldErrors.favoriteFoods }"
          @input="clearFieldError('favoriteFoods')"
          placeholder="例: 寿司,ラーメン,カレー（カンマ区切り）"
        />
        <div v-if="fieldErrors.favoriteFoods" class="error-message">
          {{ fieldErrors.favoriteFoods }}
        </div>
      </div>

      <div class="button-group">
        <button type="submit" class="btn-primary">更新</button>
        <button type="button" @click="$router.push('/users')" class="btn-secondary">
          キャンセル
        </button>
      </div>
    </form>
  </div>
</template>

<script>
export default {
  name: 'UserEditView',
  data() {
    return {
      userId: null,
      formData: {
        username: '',
        name: '',
        gender: '',
        age: null,
        postalCode: '',
        prefecture: '',
        city: '',
        address: '',
        phoneNumber: '',
        nationality: '',
      },
      favoriteFoodsInput: '',
      fieldErrors: {},
      postalCodeNotFound: false,
      countries: [],
    };
  },
  computed: {
    hasErrors() {
      return Object.keys(this.fieldErrors).length > 0 || this.postalCodeNotFound;
    }
  },
  async mounted() {
    this.userId = this.$route.params.id;
    await this.loadCountries();
    await this.loadUser();
  },
  methods: {
    async loadCountries() {
      try {
        const response = await fetch('https://restcountries.com/v3.1/all?fields=translations');
        const data = await response.json();
        
        this.countries = data
          .map(country => country.translations.jpn?.common || country.translations.jpn?.official)
          .filter(name => name)
          .sort((a, b) => a.localeCompare(b, 'ja'));
        
        const japanIndex = this.countries.indexOf('日本');
        if (japanIndex > -1) {
          this.countries.splice(japanIndex, 1);
          this.countries.unshift('日本');
        }
        
      } catch (error) {
        console.error('国名リストの取得に失敗しました:', error);
        this.countries = [
          '日本', 'アメリカ合衆国', '中国', '韓国', '台湾', 
          'イギリス', 'フランス', 'ドイツ', 'カナダ', 'オーストラリア'
        ];
      }
    },
    async loadUser() {
      try {
        const response = await fetch(`http://localhost:8081/api/users/${this.userId}`);
        
        if (!response.ok) {
          throw new Error('ユーザー情報の取得に失敗しました');
        }

        const user = await response.json();

        this.formData = {
          username: user.username,
          name: user.name,
          gender: user.gender === '男' ? '男性' : user.gender === '女' ? '女性' : user.gender,
          age: user.age,
          postalCode: user.postalCode ? user.postalCode.replace(/-/g, '') : '',
          prefecture: user.prefecture,
          city: user.city,
          address: user.address,
          phoneNumber: user.phoneNumber ? user.phoneNumber.replace(/-/g, '') : '',
          nationality: user.nationality,
        };

        this.favoriteFoodsInput = user.favoriteFoods ? user.favoriteFoods.join(',') : '';
      } catch (error) {
        console.error('ユーザー情報の取得に失敗しました:', error);
        alert('ユーザー情報の取得に失敗しました');
      }
    },
    clearFieldError(fieldName) {
      if (this.fieldErrors[fieldName]) {
        delete this.fieldErrors[fieldName];
      }
    },
    formatPostalCodeNoHyphen() {
      this.formData.postalCode = this.formData.postalCode.replace(/[^0-9]/g, '').substring(0, 7);
      this.clearFieldError('postalCode');
      this.postalCodeNotFound = false;
    },
    formatPhoneNumberNoHyphen() {
      this.formData.phoneNumber = this.formData.phoneNumber.replace(/[^0-9]/g, '').substring(0, 11);
      this.clearFieldError('phoneNumber');
    },
    async fetchAddress() {
      if (this.formData.postalCode.length === 7) {
        try {
          const response = await fetch(
            `https://zipcloud.ibsnet.co.jp/api/search?zipcode=${this.formData.postalCode}`
          );
          const data = await response.json();
          if (data.results) {
            const address = data.results[0];
            this.formData.prefecture = address.address1;
            this.formData.city = address.address2;
            this.postalCodeNotFound = false;
          } else {
            this.formData.prefecture = '';
            this.formData.city = '';
            this.postalCodeNotFound = true;
          }
        } catch (error) {
          console.error('住所取得エラー:', error);
        }
      }
    },
    async handleUpdate() {
      if (this.postalCodeNotFound) {
        this.scrollToTop();
        return;
      }
      const favoriteFoodsArray = this.favoriteFoodsInput
        ? this.favoriteFoodsInput.split(',').map(item => item.trim()).filter(item => item)
        : [];
      const requestData = {
        name: this.formData.name,
        gender: this.formData.gender,
        age: this.formData.age,
        postalCode: this.formData.postalCode,
        prefecture: this.formData.prefecture,
        city: this.formData.city,
        address: this.formData.address,
        phoneNumber: this.formData.phoneNumber,
        nationality: this.formData.nationality,
        favoriteFoods: favoriteFoodsArray,
      };
      console.log('📤 送信データ:', requestData);
      try {
        const response = await fetch(`http://localhost:8081/api/users/${this.userId}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(requestData),
        });
        const data = await response.json();
        console.log('✅ サーバー応答:', data);
        if (response.ok && data.success) {
          alert('更新が完了しました');
          this.$router.push('/users');
        } else if (data.errors) {
          this.fieldErrors = data.errors;
          this.scrollToTop();
        } else if (data.message) {
          alert(data.message);
        }
      } catch (error) {
        console.error('❌ 更新エラー:', error);
        alert('接続エラーが発生しました');
      }
    },
    scrollToTop() {
      this.$nextTick(() => {
        if (this.$refs.errorSummary) {
          this.$refs.errorSummary.scrollIntoView({ behavior: 'smooth', block: 'start' });
        } else {
          window.scrollTo({ top: 0, behavior: 'smooth' });
        }
      });
    }
  }
};
</script>

<style scoped>
.edit-container {
  max-width: 600px;
  margin: 40px auto;
  padding: 30px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
}

.error-summary {
  background-color: #ffebee;
  color: #c62828;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 20px;
  font-weight: bold;
  text-align: center;
  border: 1px solid #ef5350;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  color: #555;
  font-weight: 500;
}

.required {
  color: #f44336;
}

input[type="text"],
input[type="password"],
input[type="number"],
input[type="tel"],
select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  transition: border-color 0.3s;
}

input:focus,
select:focus {
  outline: none;
  border-color: #4CAF50;
}

input:disabled {
  background-color: #f5f5f5;
  cursor: not-allowed;
}

.error-input {
  background-color: #ffebee !important;
  border-color: #f44336 !important;
}

.error-message {
  color: #f44336;
  font-size: 13px;
  margin-top: 5px;
}

.radio-label {
  display: inline-block;
  margin-right: 20px;
  font-weight: normal;
  cursor: pointer;
}

.radio-label input[type="radio"] {
  margin-right: 5px;
  width: auto;
}

.button-group {
  display: flex;
  gap: 10px;
  margin-top: 30px;
}

.btn-primary,
.btn-secondary {
  flex: 1;
  padding: 12px;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-primary {
  background-color: #4CAF50;
  color: white;
}

.btn-primary:hover {
  background-color: #45a049;
}

.btn-secondary {
  background-color: #757575;
  color: white;
}

.btn-secondary:hover {
  background-color: #616161;
}
</style>