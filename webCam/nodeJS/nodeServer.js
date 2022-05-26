const express = require('express');
const request = require('request');
const app = express();

const server = app.listen(9090, () => {
    console.log('Start Server : localhost:9090');
});

console.log('start');

app.set('views', __dirname + '/views'); //dirname == 현재 디렉토리
app.set('view engine', 'ejs'); // 엔진을 ejs로 설정
app.engine('html', require('ejs').renderFile); // html 사용, ejs를 가져옴

app.get('/', getTest);

function getTest(req, res){
    let id = req.query.id;

    const option = {
        url : "http://210.117.128.200:8080/SeniorValidate",
        qs:{
            id : id
        }
    }

    request(option, function(error, response, html) {
        if(error) {throw error}
        const checkAccess = html.trim();

        if(checkAccess === 'true'){
            res.redirect("http://211.117.125.107:34526/");
        }
        else{
            res.status(404).render('errorPage.html');
        }
    });
}

app.all('*',function(req, res){
    res.status(404).render('errorPage.html');
});
