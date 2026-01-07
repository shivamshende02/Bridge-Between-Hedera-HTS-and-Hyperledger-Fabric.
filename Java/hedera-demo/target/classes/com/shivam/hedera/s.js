// quick-hlf-server.js
const express = require('express');
const app = express();
app.use(express.json());

app.post('/api/v1/mint', (req, res) => {
  console.log("🔥 Received from Hedera:", req.body);
  res.status(200).send({ received: true });
});

app.listen(8080, () => console.log('🌉 HLF Bridge listening on port 8080'));
