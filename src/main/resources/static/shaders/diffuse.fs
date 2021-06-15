#version 300 es
in lowp vec3 FragPos;
in lowp vec3 Normals;
out lowp vec4 FragColor;

lowp vec3 LightPos = vec3(1.0,1.0,1.0);
lowp vec3 LightColor = vec3(1.0,1.0,1.0);
lowp float AmbientStrength = 0.1f;
lowp float SpecularStrength = 0.5f;
lowp vec3 ViewPosition = vec3(0,0,0); //should be ok. Model is translated to -7

void main(void)
{
    lowp vec3 ambient = AmbientStrength * LightColor;
    lowp vec3 norm = normalize(Normals);
    lowp vec3 LightDir = normalize(LightPos - FragPos);
    //lowp vec3 ViewDir = normalize(ViewPosition - FragPos);


    lowp float diff = dot(norm, LightDir);
    lowp vec3 diffuse = diff * LightColor;

    lowp vec3 tempColor = (ambient + diffuse) * vec3(1.0, 1.0,1.0);
    FragColor = vec4(tempColor,1.0f);
}