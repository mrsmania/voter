class Environment {
  production = false;
  backendUrl = 'http://localhost:8080'; // dont forget to add the port!
  apiUrl = `${this.backendUrl}/api`;
}

export const environment = new Environment();
