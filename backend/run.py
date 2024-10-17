from flask import Flask, jsonify

app = Flask(__name__)

num = 0

@app.route("/test", methods=['GET'])
def hello_world():
    global num
    num += 1
    return jsonify({"message": num})

if __name__ == "__main__":
    app.run(debug=True)