# mrmate
Modbus REST Mate (mrmate) is a software to establish a Modbus (TCP, RTU, ASCII) communication over RESTful service


#### Key Feature:
- Consume and produce XML or JSON data format according to request header
- CORS header response with wildcard for hassle free development
- Automatic communication connection recovery
- Easy host port setup option



#### Read Data
HTTP GET Method

- Read Single Bit <br />
`http://{host address}:{port}/mrmate/read/{uri name}/{unit id}/{0/1}/{offset}`
- Read Multiple Bits <br />
`http://{host address}:{port}/mrmate/read/{uri name}/{unit id}/{0/1}/{offset}/{count}`
- Read Single Register <br />
`http://{host address}:{port}/mrmate/read/{uri name}/{unit id}/{3/4}/{offset}`
- Read Multiple Register <br />
`http://{host address}:{port}/mrmate/read/{uri name}/{unit id}/{3/4}/{offset}/{count}`




