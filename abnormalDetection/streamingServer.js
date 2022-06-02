const express = require('express')
const fs = require('fs')
const path = require('path')
const app = express()
const url = require('url')
const spawn = require('child_process').spawn
const port = 5000
 
app.get('/', function(req, res) {
    res.sendFile(path.join(__dirname + '/index.html'))
})
 
app.get('/*', function(req, res) {
    const {pathname} = url.parse(req.url, true)
    const path = `abnormalVideo/${pathname}`
    const stat = fs.statSync(path)
    const fileSize = stat.size
    const range = req.headers.range
 
    if (range) {
    const parts = range.replace(/bytes=/, "").split("-")
    const start = parseInt(parts[0], 10)
    const end = parts[1]
    ? parseInt(parts[1], 10)
    : fileSize-1
    
    const chunksize = (end-start)+1
    const file = fs.createReadStream(path, {start, end})
    const head = {
        'Content-Range': `bytes ${start}-${end}/${fileSize}`,
        'Accept-Ranges': 'bytes',
        'Content-Length': chunksize,
        'Content-Type': 'video/mp4',
    }
    
    res.writeHead(206, head)
    file.pipe(res)
    } else {
    const head = {
        'Content-Length': fileSize,
        'Content-Type': 'video/mp4',
    }
        res.writeHead(200, head)
        fs.createReadStream(path).pipe(res)
    }
})

app.get('/post/:user_id/:rasp_ip', (request, response) => {
    if( !request.params.user_id && !request.params.rasp_ip ){
        response.status(404).send('parameter error')
    } else
        result = spawn('python', ['start_model.py', request.params.user_id, request.params.rasp_ip])
        result.stdout.on('data', function(data) {
            console.log(data.toString());
        });
})
 
app.listen(port,  () => {
    console.log(`App is running on port ${port}`)
})