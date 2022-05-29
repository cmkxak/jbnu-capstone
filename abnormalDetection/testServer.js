const express = require('express')
const app = express()
const port = 5000
const spawn = require('child_process').spawn

app.get('/post/:user_id/:rasp_ip', (request, response) => {
    if( !request.params.user_id && !request.params.rasp_ip ){
        response.status(404).send('parameter error')
    } else
        result = spawn('python', ['start_model.py', request.params.user_id, request.params.rasp_ip])
        result.stdout.on('data', function(data) {
            console.log(data.toString());
        });
})

app.listen(port, () => {
    console.log(`Example app listening on port ${port}`)
})
