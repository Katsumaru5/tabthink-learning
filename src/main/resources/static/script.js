new Vue({
  el: '#app',
  data: {
    text: '未実行'
  },
  methods: {
   callApi() {
	fetch('/helloapi')
	  .then(res => res.text())
	  .then(text => {
	    this.text = text;
	  })
	  .catch(err => {
	    console.error(err);
	    this.text = 'エラー';
	  });
    }
  }	
});