from flask import Flask, request, jsonify
import joblib
import os

app = Flask(__name__)
MODEL_PATH = os.getenv('MODEL_PATH','ai/model.joblib')
model = None
if os.path.exists(MODEL_PATH):
    model = joblib.load(MODEL_PATH)

@app.route('/predict', methods=['POST'])
def predict():
    if model is None:
        return jsonify({'error':'model not found'}), 500
    features = request.json.get('features')
    prob = model.predict_proba([features])[0,1]
    return jsonify({'prob': float(prob)})

if __name__=='__main__':
    app.run(host='0.0.0.0', port=5001)