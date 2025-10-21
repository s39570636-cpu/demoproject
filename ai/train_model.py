"""
Train a simple sklearn model for occupancy prediction from CSV and save with joblib.
"""
import pandas as pd
from sklearn.ensemble import RandomForestClassifier
import joblib

def train(csv_path='data/occupancy.csv', model_path='ai/model.joblib'):
    df = pd.read_csv(csv_path)
    X = df[['temperature','light','motion']]
    y = df['occupancy']
    clf = RandomForestClassifier(n_estimators=50, random_state=42)
    clf.fit(X, y)
    joblib.dump(clf, model_path)
    print('Saved model to', model_path)

if __name__=='__main__':
    train()