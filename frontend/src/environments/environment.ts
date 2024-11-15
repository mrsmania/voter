class Environment {
  production = false;
  backendUrl = 'http://localhost:8080';
  apiUrl = `${this.backendUrl}/api`;
}

export const environment = new Environment();
